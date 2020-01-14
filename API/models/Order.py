import base64
from bson.objectid import ObjectId
from bson.son import SON

from .Drink import Drink
from init import mongo_db, app

orders = mongo_db.orders


class Order:

    """
    Attributes:
      swagger_types (dict): The key is attribute name
                            and the value is attribute type.
      attribute_map (dict): The key is attribute name
                            and the value an explanation of the attribute role.
    """
    swagger_types = {
        '_id': 'str',
        'user_id': 'str',
        'machine_id': 'str',
        'state': 'str',
        'drinks': 'list of drinks',
    }

    attribute_map = {
        '_id': 'ID pf the event',
        'user_id': 'ID of the user that has created the order',
        'machine_id': 'ID of the machine that will make the drink',
        'state': 'state of the order can be \'available\', \'processing\' or \'done\'',
        'drinks': 'list of drink object that will be embeded documents',
    }

    states = ['not_payed', 'available', 'processing', 'done']

    def __init__(self, user_id, drinks, payed=False):
        self.user_id = user_id
        self.machine_id = None
        self.state = self.states[1] if payed else self.states[0]
        self.__drinks = drinks
        self.__price = sum([drink.price for drink in drinks])
        self.payed = payed

    def create(self):
        new_event = {
            'user_id': self.user_id,
            'machine_id': self.machine_id,
            'state': self.state,
            'drinks': [SON(drink.to_dict()) for drink in self.__drinks],
            'price': self.__price,
            'payed': self.payed
        }
        self._id = orders.insert_one(new_event).inserted_id

    def append_drink(self, drink):
        self.__drinks.append(drink)
        self.__price = sum([drink.price for drink in self.drinks])

    def get_drinks(self):
        return self.__drinks

    def get_price(self):
        return self.__price

    def assigne_machine(self, machine_id):
        query = {
            "_id": self._id,
            "state": self.states[1]
        }
        updated_order = orders.update_one(query,
                                          {'$set':{
                                              'state': self.states[2],
                                              'machine_id': machine_id
                                          }})
        if updated_order.modified_count > 0:
            self.state = self.states[2]
            self.machine_id = machine_id

    def set_done(self):
        query = {
            "_id": self._id,
            "state": self.states[2]
        }
        updated_order = orders.update_one(query,
                                          {'$set':{
                                              'state': self.states[3]
                                          }})
        if updated_order.modified_count > 0:
            self.state = self.states[3]

    @staticmethod
    def get_price_from_drinks(drinks):
        return sum([drink.price for drink in drinks])

    @classmethod
    def find_by_id(cls, order_id):
        found_order_data = orders.find_one({"_id": ObjectId(order_id)})
        if found_order_data is None:
            return None
        drinks = [
                    Drink(drink.get('mixer_type'),
                          drink.get('alcohol_type'),
                          drink.get('double')) for drink in found_order_data.get('drinks')
                 ]
        found_order = cls(found_order_data.get('user_id'),
                          drinks,
                          found_order_data.get('payed'))
        found_order._id = found_order_data.get('_id')
        found_order.state = found_order_data.get('state')
        found_order.machine_id = found_order_data.get('machine_id')
        found_order.__price = found_order_data.get('price')
        found_order.payed = found_order_data.get('payed')
        return found_order

    @classmethod
    def find(cls, order_key):
        _id = base64.urlsafe_b64decode(order_key.encode('utf8')).decode('utf8')
        found_order_data = orders.find_one({"_id": ObjectId(_id)})
        if found_order_data is None:
            return None
        drinks = [
                    Drink(drink.get('mixer_type'),
                          drink.get('alcohol_type'),
                          drink.get('double')) for drink in found_order_data.get('drinks')
                 ]
        found_order = cls(found_order_data.get('user_id'),
                          drinks,
                          found_order_data.get('payed'))
        found_order._id = found_order_data.get('_id')
        found_order.state = found_order_data.get('state')
        found_order.machine_id = found_order_data.get('machine_id')
        found_order.__price = found_order_data.get('price')
        found_order.payed = found_order_data.get('payed')
        return found_order

    def to_dict(self):
        order_key_str = str(str(self._id)).encode('utf8')
        return {
            'order_key': base64.urlsafe_b64encode(order_key_str).decode('utf8'),
            'user_id': str(self.user_id),
            'machine_id': str(self.machine_id),
            'state': self.state,
            'drinks': [drink.to_dict() for drink in self.__drinks],
            'price': self.__price,
            'payed': self.payed
        }