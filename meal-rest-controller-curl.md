Curls for check Meal Rest Controller
==================================================

GET ALL:
```
curl  http://localhost:8080/topjava/rest/meals
```
GET BETWEEN: 
```
curl http://localhost:8080/topjava/rest/meals/filter?startDate=2020-01-31&endDate=2020-01-31&startTime=13:00&endTime=20:01
```
BETWEEN WITH NULL: 
```
curl http://localhost:8080/topjava/rest/meals/filter?startDate=&endTime=
```
GET MEAL with id 100003: 
```
curl http://localhost:8080/topjava/rest/meals/100003
```
POST create NEW MEAL: 
```
curl -X POST -H "Content-Type: application/json; charset=ISO-8859-1" -d '{"id": null, "dateTime": "2020-01-31T13:30:00", "description": "Новый Обед", "calories": 1000, "user": null}' http://localhost:8080/topjava/rest/meals
```
PUT update MEAL with id 100004: 
```
curl -X PUT -H "Content-Type: application/json; charset=ISO-8859-1" -d '{"id": 100004, "dateTime": "2020-01-31T13:00:02", "description": "Обновленнный Обед", "calories": 1000, "user": null}' http://localhost:8080/topjava/rest/meals/100004
```
DELETE MEAL with id 100003: 
```
curl -X DELETE http://localhost:8080/topjava/rest/meals/100003
```