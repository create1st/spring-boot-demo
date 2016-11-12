# Spring-Boot + Spring-Validation + Actuator + Swagger

This is simply DEMO app to demonstrate Spring-Validation.

## Ticket JSON format
```
{
  "currency": "USD",
  "expectedDate": "2016-01-01",
  "expiryDate": "2016-01-01",
  "externalSource": "External source",
  "price": "10.00",
  "sourceSystem": "SYSTEM_03",
  "ticketDate": "2016-01-01",
  "ticketType": "EXTERNAL"
}
```

## Swagger
Swagger can be accessed via http://127.0.0.1:8080/swagger and provides access to Actuator API

## Auth Postman methods
```
{  
   "id":"639fad06-1991-ee48-c24b-af4e0fa6cfd2",
   "name":"Ticket",
   "description":"",
   "order":[  
      "b14a07c5-5e2b-f982-7548-3a1d0d94bcce",
      "b58d0442-9954-022b-f460-efd64817c68d"
   ],
   "folders":[  

   ],
   "timestamp":1478959671389,
   "owner":0,
   "public":false,
   "requests":[  
      {  
         "id":"b14a07c5-5e2b-f982-7548-3a1d0d94bcce",
         "headers":"Authorization: Basic Y2xpZW50OnNlY3JldA==\n",
         "url":"http://127.0.0.1:8080/oauth/token?username=admin&password=secret&grant_type=password",
         "pathVariables":{  

         },
         "preRequestScript":null,
         "method":"POST",
         "collectionId":"639fad06-1991-ee48-c24b-af4e0fa6cfd2",
         "data":[  

         ],
         "dataMode":"params",
         "name":"Admin login",
         "description":"",
         "descriptionFormat":"html",
         "time":1478959719669,
         "version":2,
         "responses":[  

         ],
         "tests":"",
         "currentHelper":"normal",
         "helperAttributes":{  

         }
      },
      {  
         "id":"b58d0442-9954-022b-f460-efd64817c68d",
         "headers":"Authorization: Bearer e0286b2f-cc42-4a6e-98b2-c4ef0f539734\nContent-Type: application/json\n",
         "url":"http://127.0.0.1:8080/validator/ticket",
         "preRequestScript":null,
         "pathVariables":{  

         },
         "method":"POST",
         "data":[  

         ],
         "dataMode":"raw",
         "version":2,
         "tests":"",
         "currentHelper":"normal",
         "helperAttributes":{  

         },
         "time":1478960148229,
         "name":"Send ticket",
         "description":"",
         "collectionId":"639fad06-1991-ee48-c24b-af4e0fa6cfd2",
         "responses":[  

         ],
         "rawModeData":"{\r\n  \"currency\": \"USD\",\r\n  \"expectedDate\": \"2016-01-01\",\r\n  \"expiryDate\": \"2016-01-01\",\r\n  \"externalSource\": \"External source\",\r\n  \"price\": \"10.00\",\r\n  \"sourceSystem\": \"SYSTEM_03\",\r\n  \"ticketDate\": \"2016-01-01\",\r\n  \"ticketType\": \"EXTERNAL\"\r\n}"
      }
   ]
}
```