
# API

API is a powerful Restfull API that uses the flask framework, allows to manage users, orders, machines and events in the mongo database.

## Table of Contents

- [API](#api)
  - [Table of Contents](#table-of-contents)
  - [Restful API](#restful-api)
    - [Create new Event](#create-new-event)
    - [Create new User](#create-new-user)
    - [User Login](#user-login)
    - [Get User](#get-user)
    - [Add User to Event](#add-user-to-event)
    - [Create New Order](#create-new-order)
    - [Create New Machine](#create-new-machine)
    <!-- - [Add Machine to Event](#add-machine-to-event) -->
    - [Machine Get Order in Event](#machine-get-order-in-event)
    - [Machine Post Order Completed](#machine-post-order-completed)

## Restful API

---------------------------

### Create new Event

```JSON
POST /api/v1/event
headers = Authorization: Token Authentication_user_token
json =  {
    "name":"",
    "location":""
}
```

```JSON
RETURNS:
json = {
    "event_key": "",
    "name": "",
    "location": ""
}
```

[:top:](#table-of-contents)

### Create new User

```JSON
POST /api/v1/user
json = {
    "name":"",
    "email":"example@gmail.com",
    "password":""
}
```

```JSON
RETURNS:
json = {
    "name": "",
    "email": "",
    "token": null,
    "orders": []
}
```

[:top:](#table-of-contents)

### User Login

```JSON
GET /api/v1/user/token
json = {
    "email":"",
    "password":""
}
```

```JSON
RETURNS:
json = {
    "name": "",
    "email": "",
    "token": "",
    "orders": [
        ""
    ]
}
```

[:top:](#table-of-contents)

### Get User

```JSON
GET /api/v1/user
headers = Authorization: Token Authentication_user_token
```

```JSON
RETURNS:
json = {
    "name": "",
    "email": "",
    "token": "",
    "orders": [
        ""
    ]
}
```

[:top:](#table-of-contents)

### Add User to Event

```RESQUEST
POST /api/v1/user/event/<string:event_key>
headers = Authorization: Token Authentication_user_token
```

```JSON
RETURNS:
json = {
    "event_key": "",
    "name": "",
    "location": ""
}
```

[:top:](#table-of-contents)

### Create New Order

```JSON
POST /api/v1/order
headers = 
    Content-Type: application/json
    Authorization: Token Authentication_user_token
json={
    "event_key": "",
	"drinks": [
		{
          "mixer_type": "",
          "alcohol_type": "",
          "double": false
      },
      {
          "mixer_type": "",
          "alcohol_type": "",
          "double": true
      }
     ]
}
```

```JSON
RETURNS:
json = {
    "order_key": "",
    "user_id": "",
    "machine_id": "",
    "state": "",
    "drinks": [
        {
            "mixer_type": "",
            "alcohol_type": "",
            "double": true,
            "price": 3.5
        },
        {
            "mixer_type": "",
            "alcohol_type": "",
            "double": true,
            "price": 3.5
        }
    ],
    "price": 7.0,
    "payed": true
}
```

[:top:](#table-of-contents)

### Create New Machine

```JSON
POST /api/v1/machine/<string:event_key>
headers = Authorization: Token Authentication_user_token
```

```JSON
RETURNS:
json = {
    "machine_key": "",
    "token": "",
    "selected_order": null,
    "processed_orders": [],
    "state": "ok",
    "error": null
}
```

<!-- [:top:](#table-of-contents)

### Add Machine to Event

```REQUEST
POST /api/v1/machine/event/<string:event_key>
header Authorization: Token Authentication_machine_token
```

```JSON
RETURNS:
json = {
    "event_key": "",
    "name": "",
    "location": ""
}
``` -->

[:top:](#table-of-contents)

### Machine Get Order in Event

```REQUEST
GET /api/v1/machine/order/<string:event_key>/<string:order_key>
header Authorization: Token Authentication_machine_token
```

```JSON
RETURNS:
json = {
    "order_key": "",
    "user_id": "",
    "machine_id": "",
    "state": "processing",
    "drinks": [
        {
            "mixer_type": "",
            "alcohol_type": "",
            "double": true,
            "price": 3.5
        },
        {
            "mixer_type": "",
            "alcohol_type": "",
            "double": true,
            "price": 3.5
        }
    ],
    "price": 7.0,
    "payed": true
}
```

[:top:](#table-of-contents)

### Machine Post Order Completed

```REQUEST
POST /api/v1/machine/order/done/<string:event_key>
header Authorization: Token Authentication_machine_token
```

```JSON
RETURNS:
json = {
    "order_key": "NWRjZGZjNGNhMzhmMDcwMzlmYWJhZGJk",
    "user_id": "5dcb282a97f05337ea18fa3e",
    "machine_id": "5dcdfc8da38f07039fabadbe",
    "state": "done",
    "drinks": [
        {
            "mixer_type": "coke",
            "alcohol_type": "rhum",
            "double": true,
            "price": 3.5
        },
        {
            "mixer_type": "coke",
            "alcohol_type": "rhum",
            "double": true,
            "price": 3.5
        }
    ],
    "price": 7.0,
    "payed": true
}
```

[:top:](#table-of-contents)
