import bcrypt
import datetime
import base64
from bson.objectid import ObjectId

from init import mongo_db, app

machines = mongo_db.machines


class Machine_Creation_Exception(Exception):
    message = "Cannot create machine"


def __getBase64(my_str):
    return base64.urlsafe_b64encode(str(my_str).encode('utf8')).decode('utf8')


class Machine():

    """
    Attributes:
      swagger_types (dict): The key is attribute name
                            and the value is attribute type.
      attribute_map (dict): The key is attribute name
                            and the value an explanation of the attribute role.
    """
    swagger_types = {
        '_id': 'str',
        'machine_key': 'str',
        'token': 'str',
        'selected_order': 'str',
        'processed_orders': 'str',
        'state': 'str',
        'error': 'str'
    }

    attribute_map = {
        '_id': 'ID of the user',
        'machine_key': 'the unique key of the machine',
        'token': 'token used to validate the user is signed in',
        'selected_order': 'ID of the order that is being processes',
        'processed_orders': 'list of orde IDs that have been processed',
        'state': 'state of the machine',
        'error': 'error that might have occured on the machine'
    }

    states = ['ok', 'empty', 'error']

    def __init__(self):
        self.machine_key = None
        self.token = None
        self.selected_order = None
        self.processed_orders = []
        self.state = self.states[0]
        self.error = None

    def create(self, user):
        if not user.admin:
            raise Machine_Creation_Exception
        str_token = 'machine:'+str(datetime.datetime.now())
        self.token = 'machine'+base64.urlsafe_b64encode(
            str_token.encode('utf8')).decode('utf8')
        new_machine = {
            'token': self.token,
            'selected_order': self.selected_order,
            'processed_orders': self.processed_orders,
            'state': self.state,
            'error': self.error,
        }
        self._id = machines.insert_one(new_machine).inserted_id
        self.machine_key = base64.urlsafe_b64encode(
            str(self._id).encode('utf8')).decode('utf8')

    def set_selected_order(self, order):
        query = {
            "_id": self._id,
            "selected_order": None
        }
        updated_order = machines.update_one(query,
                                            {'$set': {'selected_order': order._id}})
        if updated_order.modified_count > 0:
            self.selected_order = order._id

    def set_selected_order_done(self):
        query = {
            "_id": self._id,
            "selected_order": {'$ne': None}
        }
        updated_order = machines.update_one(query,
                                            {
                                                '$addToSet': {'processed_orders': self.selected_order},
                                                '$set': {'selected_order': None}
                                            })
        if updated_order.modified_count > 0:
            self.processed_orders.append(self.selected_order)
            self.selected_order = None

    @classmethod
    def find(cls, machine_key):
        _id = base64.urlsafe_b64decode(
            machine_key.encode('utf8')).decode('utf8')
        found_machine = machines.find_one({'_id': ObjectId(_id)})
        if found_machine is None:
            return None
        machine = cls()
        machine._id = found_machine.get("_id")
        machine.machine_key = base64.urlsafe_b64encode(
            str(machine._id).encode('utf8')).decode('utf8')
        machine.token = found_machine.get("token")
        machine.selected_order = found_machine.get("selected_order")
        machine.processed_orders = found_machine.get("processed_orders")
        machine.state = found_machine.get("state")
        machine.error = found_machine.get("error")
        return machine

    @classmethod
    def find_by_token(cls, token):
        found_machine = machines.find_one({'token': token})
        if found_machine is None:
            return None
        machine = cls()
        machine._id = found_machine.get("_id")
        machine.machine_key = base64.urlsafe_b64encode(
            str(machine._id).encode('utf8')).decode('utf8')
        machine.token = found_machine.get("token")
        machine.selected_order = found_machine.get("selected_order")
        machine.processed_orders = found_machine.get("processed_orders")
        machine.state = found_machine.get("state")
        machine.error = found_machine.get("error")
        return machine

    def __getBase64(self, my_str):
        return base64.urlsafe_b64encode(str(my_str).encode('utf8')).decode('utf8')

    def to_dict(self, withToken=True):
        if withToken:
            return {
                'machine_key': self.machine_key,
                'token': self.token,
                'selected_order': [] if self.selected_order is None else [self.__getBase64(selected_order) for selected_order in self.selected_order],
                'processed_orders': [] if self.processed_orders is None else [self.__getBase64(processed_order) for processed_order in self.processed_orders],
                'state': self.state,
                'error': self.error,
            }
        return {
            'machine_key': self.machine_key,
            'selected_order': [] if self.selected_order is None else [self.__getBase64(selected_order) for selected_order in self.selected_order],
            'processed_orders': [] if self.processed_orders is None else [self.__getBase64(processed_order) for processed_order in self.processed_orders],
            'state': self.state,
            'error': self.error,
        }
