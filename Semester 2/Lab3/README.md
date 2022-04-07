### Для запуска из идеи на разных портах настроить в конфигурациях запуска для спринга VM options

-Dspring.profiles.active=pass

-Dspring.profiles.active=report

-Dspring.profiles.active=membership

### Примеры запросов
1) 
   * http://localhost:8081/deliver-membership?id=1&from=2020-03-18&to=2020-03-20
   * http://localhost:8081/prolong-membership?id=1&date=2022-03-25
   * http://localhost:8081/info?id=1


2) * http://localhost:8082/can-enter?id=1
   * http://localhost:8082/went-out?id=1
   

3) * http://localhost:8083/date-stats
   * http://localhost:8083/average-stats

