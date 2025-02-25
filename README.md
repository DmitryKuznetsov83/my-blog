# My Blog

### Как собирать приложение
- Используем gradle wrapper

### Как запускать тесты
Есть следующие тесты
- Тесты сервисов с замокаными репозиториями.
    - **Где**: ru.yandex.practicum.service
- Тесты контроллеров
    - Основано на MockMvc
    - Проверяем возвращаемые HTML странички
- Тесты репозиториев

### Приложение запускает через как стандартное Spring Boot приложение
- класс для запуска MyBlogSpringBootApplication
- используется embedded контейнер сервлетов Tomcat

### Как запускать приложение[MyBlogSpringBootApplication.java](src/main/java/ru/yandex/practicum/MyBlogSpringBootApplication.java)
- открываем в браузере http://localhost:8080/myblog/posts

