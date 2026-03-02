1. 数据库表名 / 字段名不匹配
   现象：
   报错：Unknown column 'name' in 'where clause'
   或：Table 'first.user' doesn't exist
   原因：
   SQL 语句中写的表名 / 字段名，和数据库 users 表的实际结构不一致。
   例如：SQL 用 name，但表中字段是 username；SQL 用 user，但表名是 users。
   解决步骤：
   执行 DESC users; 查看表结构，确认真实字段名。
   修改 Mapper 中的 SQL 语句，将 name 改为 username，user 改为 users。
   若存在 XML 映射文件，优先修改或删除 XML，避免覆盖注解 SQL。
2. JJWT 版本不兼容
   现象：
   报错：NoSuchMethodError: 'void io.jsonwebtoken.lang.Assert.notNull(...)'
   或：Jwts.builder()、Jwts.SIG.HS256 等 API 找不到。
   原因：
   JJWT 0.11.x 和 0.12.x 的 API 不兼容，且依赖版本必须完全一致。
   代码是按 0.12.x 写的，但依赖是 0.11.5，导致方法签名不匹配。
   解决步骤：
   方案一（推荐）：回退到 0.11.5
   保持 pom.xml 中 jjwt-api、jjwt-impl、jjwt-jackson 版本为 0.11.5。
   修改代码适配 0.11.5 API：
   java
   运行
   String token = Jwts.builder()
   .setSubject(user.getUsername()) // 用 setSubject() 代替 subject()
   .setExpiration(new Date(System.currentTimeMillis() + 3600000)) // 用 setExpiration() 代替 expiration()
   .signWith(SignatureAlgorithm.HS256, jwtKey) // 用 SignatureAlgorithm.HS256 代替 Jwts.SIG.HS256
   .compact();
   方案二：升级到 0.12.x
   统一依赖版本到 0.12.x。
   修改密钥生成逻辑，适配新 API。
3. Jackson 依赖缺失 / 冲突
   现象：
   报错：ClassNotFoundException: com.fasterxml.jackson.core.JsonProcessingException
   或：NoClassDefFoundError: com/fasterxml/jackson/core/JsonProcessingException
   原因：
   jjwt-jackson 依赖 Jackson，但项目中未引入或版本不一致。
   手动引入的 Jackson 版本（如 2.20、2.20.2）不统一，导致冲突。
   解决步骤：
   在 pom.xml 中显式添加 Jackson 依赖，并统一版本（推荐 2.15.2）：
   xml
   <dependency>
   <groupId>com.fasterxml.jackson.core</groupId>
   <artifactId>jackson-core</artifactId>
   <version>2.15.2</version>
   </dependency>
   <dependency>
   <groupId>com.fasterxml.jackson.core</groupId>
   <artifactId>jackson-databind</artifactId>
   <version>2.15.2</version>
   </dependency>
   <dependency>
   <groupId>com.fasterxml.jackson.core</groupId>
   <artifactId>jackson-annotations</artifactId>
   <version>2.15.2</version>
   </dependency>
   执行 mvn clean，刷新 Maven 依赖，重启项目。
4. 序列化实现缺失
   现象：
   报错：UnknownClassException: Unable to find an implementation for interface io.jsonwebtoken.io.Serializer
   原因：
   移除了 jjwt-jackson 依赖，但 JJWT 需要序列化实现。
   解决步骤：
   恢复 jjwt-jackson 依赖，版本与其他 JJWT 依赖一致。
   或在代码中显式指定默认序列化器：
   java
   运行
   .serializeToJsonWith(new DefaultSerializer<>())
   三、排错方法论总结
   先看日志，定位根因：优先关注 Caused by 后的异常，这是问题的核心。
   版本优先，统一管理：三方依赖版本不一致是最大的坑，务必保证同系列依赖版本完全相同。
   清理缓存，重建项目：mvn clean + 重启 IDE，能解决 30% 的诡异问题。
   最小化修改，逐步验证：一次只改一个点，验证通过后再进行下一步，避免引入新问题。
   善用工具：利用 IDEA 的 Maven 面板、Database 工具，快速查看依赖和表结构。