# Campsite Availabilities

Get a list of dates for a given campsite based on a start date and end date.

**URL** : `/api/v1/campsite/:campsiteId/availabilities?startDate&endDate`

**URL Parameters** : 

| Param         | Type    | Required | Description |
| ------------- |:-------:| :-------:| :----------:|
| `campsiteId`  | number  | true     | ID of the Campsite that the user is interested.


**Query Parameters** : 

| Param        | Type              | Required | Description |
| ------------ |:-----------------:| :-------:| :----------:|
| `startDate`  | date `yyyy-MM-dd` | false    | If not provided the default value will be today;
| `endDate`   | date `yyyy-MM-dd` | false    | If not provided the default value will be today plus one month;

**Method** : `GET`

**Success Response** : 

**Code** : `200 OK`

**Content examples**

For a `campsiteId = 1` and `startDate = 2018-11-15` and `endDate 2018-11-20`

```json
{
  "results":[
    "2018-11-15",
    "2018-11-16",
    "2018-11-17",
    "2018-11-18",
    "2018-11-19"
    ]
}
```

For a `campsiteId = 1` and `startDate = invalid` and `endDate 2018-11-20`

```json
{
  "errors":[
    "Start date is not in a valid format yyyy-MM-dd."
  ],
  "status":400
}
```

# Campsite Booking

Create a new booking for a campsite

**URL** : `/api/v1/campsite/:site_id/booking`

**URL Parameters** : 

| Param         | Type    | Required | Description |
| ------------- |:-------:| :-------:| :----------:|
| `campsiteId`  | number  | true     | ID of the Campsite that the user is interested.


**Method** : `POST`

**Request Body** : 

```json
{
	"firstName": "Pedro",
	"lastName": "Coelho Torres",
	"email": "pedro@hire.me",
	"startDate": "2018-11-15",
	"endDate": "2018-11-17"
}
```

**Success Response** : 

**Condition** : If everything all the conditions are met.

**Code** : `201 CREATED`

**Content example**

```json
{
    "id": 123,
    "name": "Build something project dot com",
    "url": "http://testserver/api/accounts/123/"
}
```

**Error Response** : 

**Condition** : If fields are missed or inconsistencies on the values.

**Code** : `400 BAD REQUEST`

**Content example**

```json
{
  "errors":[
    "First name is required."
  ],
  "status":400
}
```

# Booking Operations

**URL** : `http://localhost:9090/api/v1/campsite/booking/:bookingCode`

**URL Parameters** : 

| Param         | Type    | Required | Description |
| ------------- |:-------:| :-------:| :----------:|
| `bookingCode` | string  | true     | Alphanumeric booking code.


**Method** : `GET`

**Success Response** : 

**Code** : `200 OK`

**Content examples**

For a `bookingCode = D7LP4W2018`

```json
{ 
  "results": {
    "bookingId": 2,
    "bookingCode": "D7LP4W2018",
    "camperName": "Pedro Coelho Torres",
    "status": "CONFIRMED",
    "startDate": "2018-11-19", 
    "endDate":"2018-11-20"
  },
  "status":200
}
```

**Error Response** : 

**Code** : `404 Not Found`


For a `bookingCode = 000000`

```json
{
  "errors": [
      "The booking 000000 was not found."
  ],
  "status": 404
}
```

**Method** : `PUT`

**Code** : `200 OK`

**Content examples**

For a `bookingCode = D7LP4W2018`

**Request Body** : 

```json
{
   "firstName": "Pedro",
   "lastName": "Coelho Torres",
   "email": "pedro@hire.me",
   "startDate": "2018-11-20",
   "endDate": "2018-11-22"
}
```

**Success Response** : 

```json
{ 
  "results": {
    "bookingId": 2,
    "bookingCode": "D7LP4W2018",
    "camperName": "Pedro Coelho Torres",
    "status": "CONFIRMED",
    "startDate": "2018-11-20", 
    "endDate":"2018-11-22"
  },
  "status":200
}
```

**Error Response** : 

**Code** : `404 Not Found`


For a `bookingCode = 000000`

```json
{
  "errors": [
      "The booking 000000 was not found."
  ],
  "status": 404
}
```
**URL** : `http://localhost:9090/api/v1/campsite/booking/:bookingCode/cancel`

**Method** : `PUT`

**Code** : `200 OK`

**Content examples**

For a `bookingCode = D7LP4W2018`

**Success Response** : 

```json
{ 
  "status":200
}
```

**Error Response** : 

**Code** : `404 Not Found`


For a `bookingCode = 000000`

```json
{
  "errors": [
      "The booking 000000 was not found."
  ],
  "status": 404
}
```