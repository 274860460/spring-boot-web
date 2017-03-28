package com.example.annotation;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Component
public class ResourcesParser {

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired
    private RequestMappingHandlerMapping bean;


    private static final String MODULE = "module";
    private static final String MENU = "menu";
    private static final String LINK = "link";
    private static final String BUTTON = "button";
    private static final String DEFAULT = "default";
    private static final String NULL = "";



    private Map<Module, List<LinkMateData>> loadModules() {

        Map<Module, List<LinkMateData>> data = new LinkedHashMap<>();
        Map<RequestMappingInfo, HandlerMethod> map = bean.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : map.entrySet()) {
            RequestMappingInfo info = m.getKey();
            HandlerMethod method = m.getValue();

            Module module = method.getBeanType().getAnnotation(Module.class);
            if (module == null) {
                continue;
            }

            String url = info.getPatternsCondition().toString().replace("[", "").replace("]", "");
            String methodName = method.getMethod().getName();
            Link link = method.getMethodAnnotation(Link.class);

            LinkMateData linkMateData = new LinkMateData(url, methodName, link.resourceName(), link.button(), link.hasButton());

            List<LinkMateData> handlerMethods = data.computeIfAbsent(module, k -> new ArrayList<>());
            handlerMethods.add(linkMateData);
        }
        return data;
    }

    private Set<SqlMateData> parse(Map<Module, List<LinkMateData>> data) {
        Set<SqlMateData> set = new LinkedHashSet<>();

        for (Map.Entry<Module, List<LinkMateData>> entry : data.entrySet()) {

            Module module = entry.getKey();
            String parentName = module.parent();
            String name = module.name();
            //添加父模块
            SqlMateData moduleSqlMateData = createSqlMateData(parentName, NULL, MODULE, parentName, DEFAULT, DEFAULT, DEFAULT);
            set.add(moduleSqlMateData);
            //添加自身模块
            SqlMateData menuSqlMateData = createSqlMateData(name + " " + MENU, NULL, MENU, parentName, name, DEFAULT, DEFAULT);
            menuSqlMateData.parentId = moduleSqlMateData.id;
            set.add(menuSqlMateData);

            String parentId = menuSqlMateData.id;
            for (LinkMateData linkMateData : entry.getValue()) {

                String resourceName = linkMateData.name;

                if ("".equals(resourceName)) {
                    resourceName = format(linkMateData.method, LINK);
                }

                SqlMateData linkSqlMateData = createSqlMateData(resourceName, linkMateData.url, LINK, parentName, name, LINK, linkMateData.method);
                linkSqlMateData.parentId = parentId;
                set.add(linkSqlMateData);

                if (linkMateData.hasButton) {
                    SqlMateData buttonSqlMateData = createSqlMateData(format(linkMateData.button, BUTTON), NULL, BUTTON, parentName, name, BUTTON, linkMateData.method);
                    buttonSqlMateData.parentId = parentId;
                    set.add(buttonSqlMateData);
                }
            }
        }
        return set;
    }

    private String format(String method, String type){
        return String.format("%s %s", method, type);
    }

    private SqlMateData createSqlMateData(String resourceName, String url, String type, String moduleName, String menuName, String codeType, String methodName){
        String id = String.format("%s:%s:%s:%s:%s", applicationName, moduleName, menuName, codeType, methodName);

        return new SqlMateData(id, resourceName, url, type);
    }

    private class LinkMateData {

        String url;
        String method;
        String name;
        String button;
        boolean hasButton;

        LinkMateData(String url, String method, String name, String button, boolean hasButton) {
            this.url = url;
            this.method = method;
            this.name = name;
            this.button = button;
            this.hasButton = hasButton;
        }
    }

    private class SqlMateData {

        String id;
        String name;
        String url;
        String type;
        String parentId;

        SqlMateData(String id, String name, String url, String type) {
            this.id = id;
            this.name = name;
            this.url = url;
            this.type = type;
        }

    }

    public void execute() throws IOException {
        Map<Module, List<LinkMateData>> moduleListMap = loadModules();
        Set<SqlMateData> parse = parse(moduleListMap);

        if (parse.size() == 0) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("insert into t_sys_resources (id, resource_name, url, type, parent_id) values ");
        sb.append("\r\n");

        for (SqlMateData sqlMateData : parse) {
            String sql = String.format("('%s', '%s', '%s', '%s', '%s'), \r\n", sqlMateData.id, sqlMateData.name, sqlMateData.url, sqlMateData.type, sqlMateData.parentId);
            sb.append(sql);
        }

        int lastIndex = sb.lastIndexOf(",");
        sb = sb.delete(lastIndex, sb.length());
        sb.append(";");


        String absolutePath = this.getClass().getResource("/").toString();
        absolutePath = absolutePath.replace("file:/", "");

        File path = new File(absolutePath + "/sql");
        path.mkdir();

        File sql = new File(path.getAbsolutePath() + "/resources.sql");
        sql.createNewFile();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(sql);
            out.write(sb.toString().getBytes());
            out.flush();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

}
