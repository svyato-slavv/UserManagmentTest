Техническое задание:
-----------------------------------------------------------
Сделать Rest API по управлению пользователями там можно:
•	Добавлять пользователя
•	Удалять пользователя
•	Обновлять пользователя
Набор атрибутов пользователя на усмотрение разработчика, база данных PostgreSQL, поднимать ее через Docker-compose. 
Реализовать API Gateway – доступ к Rest API пользователей должен идти через него. 
Делается единый репозиторий в котором должно получиться 3-4 модуля. 
Можно реализовать spring security в Rest API если хватит времени
------------------------------------------------------------
Было создано 4 сервиса:
 - Eureka-server
 - Api-Gateway(Единая точка входа. Порт - 777)
 - User-Service
 - Authentication-service

-- База поднимается через docker-compose
-- Запуск по очереди: Eureka-server->Api-Gateway->Authentication-service->User-service
-- Написано 8 тестов на UserController
-- Реализована Jwt аутентификация с блокировкой администратора, в случае неправильного ввода пароля 3 раза.
   Время жизни access-токена - 5 минут, refresh-token - 30 дней.
-- При запуске добавляется администратор c логином - admin паролем - admin
-- Действующие URL:
- Авторизация: POST http://localhost:777/api/v1/auth/login
Тело:
{
    "login" : "admin",
    "password" : "admin"
}
- Добавить пользователя: POST http://localhost:777/api/v1/users
  В "Authorization" добавить - access-токен
{
    "name": "username",
    "password": "user",
    "email": "user@mail.ru"
}
- Получить пользователя: GET http://localhost:777/api/v1/users/1
  В "Authorization" добавить - access-токен
- Обновить данные пользователя: PUT http://localhost:777/api/v1/users/1
  В "Authorization" добавить - access-токен
{
    "name": "username2",
    "password": "user2",
    "email": "user2@mail.ru"
}
- Удалить пользователя: DELETE http://localhost:777/api/v1/users/1
  В "Authorization" добавить - access-токен



