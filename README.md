
![github1](https://github.com/user-attachments/assets/67a2b573-54e8-4b73-af29-ec49cfab173e)

# Web chat application
This is a simple web chat application with a modern design written in spring boot with postgresql and angular.

## Feathures
- Contacts
- Settings
- Register and Login
- Google login (work in progress)
- 1 to 1 messages

### Before running the application complete the backend file - application.properties

```
spring.application.name=web-chat-back-end
spring.application.production=false
spring.application.frontend.url=http://localhost:4200

spring.datasource.url=
spring.datasource.username=
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true

security.jwt.secret-key=
security.jwt.issuer=WebChat
```

![github2](https://github.com/user-attachments/assets/4776d8ec-1815-4b1a-959f-2c5b3e84c692)
