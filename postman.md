Postman REST API testing for MealRestController, AdminRestController and ProfileRestController
```json
{
	"info": {
		"_postman_id": "a2e9b706-8edc-4fca-8323-83ae8973cc67",
		"name": "topjava",
		"schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
	},
	"item": [
		{
			"name": "MEALS CONTROLLER TEST",
			"item": [
				{
					"name": "ALL MEALS",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{rest}}/meals",
							"host": [
								"{{rest}}"
							],
							"path": [
								"meals"
							]
						}
					},
					"response": []
				},
				{
					"name": "ALL BETWEEN",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{rest}}/meals/filter?startDate=2020-01-30&endDate=2020-01-30&startTime=&endTime=",
							"host": [
								"{{rest}}"
							],
							"path": [
								"meals",
								"filter"
							],
							"query": [
								{
									"key": "startDate",
									"value": "2020-01-30"
								},
								{
									"key": "endDate",
									"value": "2020-01-30"
								},
								{
									"key": "startTime",
									"value": ""
								},
								{
									"key": "endTime",
									"value": ""
								}
							]
						}
					},
					"response": []
				},
				{
					"name": "MEAL 100003",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{rest}}/meals/100003",
							"host": [
								"{{rest}}"
							],
							"path": [
								"meals",
								"100003"
							]
						}
					},
					"response": []
				},
				{
					"name": "NEW MEAL",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									""
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \"id\": null,\r\n    \"dateTime\": \"2020-01-31T13:30:00\",\r\n    \"description\": \"Новый Обед\",\r\n    \"calories\": 1000,\r\n    \"user\": null\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "{{rest}}/meals",
							"host": [
								"{{rest}}"
							],
							"path": [
								"meals"
							]
						}
					},
					"response": []
				},
				{
					"name": "UPDATE MEAL",
					"event": [
						{
							"listen": "test",
							"script": {
								"exec": [
									""
								],
								"type": "text/javascript"
							}
						}
					],
					"request": {
						"method": "PUT",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\r\n    \"id\": 100004,\r\n    \"dateTime\": \"2020-01-31T13:00:02\",\r\n    \"description\": \"Обновленнный Обед\",\r\n    \"calories\": 1000,\r\n    \"user\": null\r\n}",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "{{rest}}/meals/100004",
							"host": [
								"{{rest}}"
							],
							"path": [
								"meals",
								"100004"
							]
						}
					},
					"response": []
				},
				{
					"name": "DELETE MEAL",
					"request": {
						"method": "DELETE",
						"header": [],
						"url": {
							"raw": "{{rest}}/meals/100003",
							"host": [
								"{{rest}}"
							],
							"path": [
								"meals",
								"100003"
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "ADMIN REST CONTROLLER TEST",
			"item": [
				{
					"name": "ALL USERS",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "http://localhost:8080/topjava/rest/admin/users",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"topjava",
								"rest",
								"admin",
								"users"
							]
						}
					},
					"response": []
				},
				{
					"name": "USER 100000",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "http://localhost:8080/topjava/rest/admin/users/100000",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"topjava",
								"rest",
								"admin",
								"users",
								"100000"
							]
						}
					},
					"response": []
				},
				{
					"name": "WITH MEALS 100000",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "{{rest}}/admin/users/100000/with-meals",
							"host": [
								"{{rest}}"
							],
							"path": [
								"admin",
								"users",
								"100000",
								"with-meals"
							]
						}
					},
					"response": []
				},
				{
					"name": "ADMIN 100001",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "http://localhost:8080/topjava/rest/admin/users/100001",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"topjava",
								"rest",
								"admin",
								"users",
								"100001"
							]
						}
					},
					"response": []
				},
				{
					"name": "NEW USER",
					"request": {
						"method": "POST",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\"name\": \"New2\",\r\n                        \"email\": \"new2@yandex.ru\",\r\n                        \"password\": \"passwordNew\",\r\n                        \"roles\": [\"USER\"]\r\n                        }",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:8080/topjava/rest/admin/users",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"topjava",
								"rest",
								"admin",
								"users"
							]
						}
					},
					"response": []
				},
				{
					"name": "NEW USER",
					"request": {
						"method": "PUT",
						"header": [],
						"body": {
							"mode": "raw",
							"raw": "{\"name\": \"updated\",\r\n                        \"email\": \"updated@yandex.ru\",\r\n                        \"password\": \"updatedPasswordNew\",\r\n                        \"roles\": [\"USER\"]\r\n                        }",
							"options": {
								"raw": {
									"language": "json"
								}
							}
						},
						"url": {
							"raw": "http://localhost:8080/topjava/rest/admin/users/100011",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"topjava",
								"rest",
								"admin",
								"users",
								"100011"
							]
						}
					},
					"response": []
				},
				{
					"name": "NEW USER",
					"request": {
						"method": "DELETE",
						"header": [],
						"url": {
							"raw": "http://localhost:8080/topjava/rest/admin/users/100011",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"topjava",
								"rest",
								"admin",
								"users",
								"100011"
							]
						}
					},
					"response": []
				},
				{
					"name": "BY EMAIL",
					"request": {
						"method": "GET",
						"header": [],
						"url": {
							"raw": "http://localhost:8080/topjava/rest/admin/users/by?email=admin@gmail.com",
							"protocol": "http",
							"host": [
								"localhost"
							],
							"port": "8080",
							"path": [
								"topjava",
								"rest",
								"admin",
								"users",
								"by"
							],
							"query": [
								{
									"key": "email",
									"value": "admin@gmail.com"
								}
							]
						}
					},
					"response": []
				}
			]
		},
		{
			"name": "PROFILE REST CONTROLLER",
			"item": [
				{
					"name": "WITH MEALS",
					"request": {
						"method": "GET",
						"header": [],
						"url": null
					},
					"response": []
				}
			]
		}
	]
}