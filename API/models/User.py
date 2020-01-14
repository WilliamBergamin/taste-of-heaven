import datetime
import base64
import bcrypt

from init import mongo_db, app

users = mongo_db.users


class User_Creation_Exception(Exception):
    message = "Email already used for existing user"


class User():

    """
    Attributes:
      swagger_types (dict): The key is attribute name
                            and the value is attribute type.
      attribute_map (dict): The key is attribute name
                            and the value an explanation of the attribute role.
    """
    swagger_types = {
        '_id': 'str',
        'name': 'str',
        'email': 'str',
        'password': 'str',
        'admin': 'bool',
        'token': 'str',
        'orders': 'list',
    }

    attribute_map = {
        '_id': 'ID pf the user',
        'name': "Name of the user",
        'email': 'Email of the user',
        'password': 'hashed password of the user',
        'admin': 'boolean stating if the client if an admin',
        'token': 'token used to validate the user is signed in',
        'orders': 'ids of the orders associated with the user'
    }

    def __init__(self, name, email, password):
        salt = bcrypt.gensalt()
        self.name = name
        self.email = email
        self.password = bcrypt.hashpw(password.encode('utf8'), salt)
        self.admin = False
        self.token = None
        self.orders = []

    def create(self):
        query = {'email': self.email}
        new_user = {
            'name': self.name,
            'email': self.email,
            'password': self.password,
            'admin': False,  # No user can be created admin.
            'token': self.token,
            'orders': self.orders
        }
        insert = users.update_one(query,
                                  {"$setOnInsert": new_user},
                                  upsert=True)
        if insert.upserted_id is None:
            raise User_Creation_Exception
        else:
            self._id = insert.upserted_id

    def save(self):
        query = {"_id": self._id}
        new_values = {"$set": {
                'name': self.name,
                'email': self.email,
                'admin': self.admin,
                'token': self.token,
                'orders': self.orders
            }
        }
        users.update_one(query, new_values)

    def get_token(self, password):
        if bcrypt.checkpw(password.encode('utf8'), self.password):
            # base 64 encoded datetime:email
            str_token = str(datetime.datetime.now())+":"+str(self._id)
            self.token = base64.urlsafe_b64encode(
                                    str_token.encode('utf8')).decode('utf8')
        else:
            self.token = None

    def add_order(self, order):
        query = {"_id": self._id, "orders": {"$ne": order._id}}
        new_values = {
            "$addToSet": {"orders": order._id}
        }
        updated_user = users.update_one(query, new_values)
        if updated_user.modified_count > 0:
            self.orders.append(order._id)

    @classmethod
    def find(cls, email):
        found_user = users.find_one({'email': email})
        if found_user is None:
            return None
        user = cls(found_user.get("name"),
                   found_user.get("email"),
                   "")
        user._id = found_user.get("_id")
        user.password = found_user.get("password")
        user.admin = found_user.get("admin")
        user.token = found_user.get("token")
        user.orders = found_user.get("orders")
        return user

    @classmethod
    def find_by_token(cls, token):
        found_user = users.find_one({'token': token})
        if found_user is None:
            return None
        user = cls(found_user.get("name"),
                   found_user.get("email"),
                   "")
        user._id = found_user.get("_id")
        user.password = found_user.get("password")
        user.admin = found_user.get("admin")
        user.token = found_user.get("token")
        user.orders = found_user.get("orders")
        return user

    def to_dict(self):
        return {
            'name': self.name,
            'email': self.email,
            'token': self.token,
            'orders': [str(order) for order in self.orders],
        }
