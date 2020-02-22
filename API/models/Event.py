import base64
from bson.objectid import ObjectId
from init import mongo_db

events = mongo_db.events


class Order_Process_Exception(Exception):
    message = "Exception in order process"


class Event():

    """
    Attributes:
      swagger_types (dict): The key is attribute name
                            and the value is attribute type.
      attribute_map (dict): The key is attribute name
                            and the value an explanation of the attribute role.
    """
    swagger_types = {
        '_id': 'ID',
        'name': 'str',
        'location': 'str',
        'users': 'list',
        'new_orders': 'list',
        'pending_orders': 'list',
        'processed_orders': 'list',
        'machines': 'list'
    }

    attribute_map = {
        '_id': 'id of the event',
        'name': 'name of the event',
        'location': 'location of the address',
        'users': ' list of user ids that are attending this event',
        'new_orders': 'list of active orders in the event',
        'pending_orders': 'list of orders that are being processed by machine',
        'processed_orders': 'list of orders that were processed',
        'machines': 'list of machines registered to this event'
    }

    def __init__(self, name, location):
        self.name = name
        self.location = location
        # self.start_date = start_date
        # self.end_date = end_date
        self.users = []
        self.new_orders = []
        self.pending_orders = []
        self.processed_orders = []
        self.machines = []

    def create(self):
        new_event = {
            'name': self.name,
            'location': self.location,
            'users': self.users,
            'new_orders': self.new_orders,
            'pending_orders': self.pending_orders,
            'processed_orders': self.processed_orders,
            'machines': self.machines
        }
        self._id = events.insert_one(new_event).inserted_id

    def save(self):
        query = {"_id": self._id}
        new_values = {"$set": {
            'name': self.name,
            'location': self.location,
            'users': self.users,
            'new_orders': self.new_orders,
            'pending_orders': self.pending_orders,
            'processed_orders': self.processed_orders,
            'machines': self.machines
        }
        }
        events.update_one(query, new_values)

    def add_user(self, user):
        query = {"_id": self._id, "users": {"$ne": user._id}}
        new_values = {
            "$addToSet": {"users": user._id}
        }
        updated_event = events.update_one(query, new_values)
        if updated_event.modified_count > 0:
            self.users.append(user._id)

    def add_machine(self, machine):
        query = {"_id": self._id, "users": {"$ne": machine._id}}
        new_values = {
            "$addToSet": {"machines": machine._id}
        }
        updated_event = events.update_one(query, new_values)
        if updated_event.modified_count > 0:
            self.machines.append(machine._id)

    def add_new_order(self, order):
        query = {"_id": self._id, "new_orders": {"$ne": order._id}}
        new_values = {
            "$addToSet": {"new_orders": order._id}
        }
        updated_event = events.update_one(query, new_values)
        if updated_event.modified_count > 0:
            self.new_orders.append(order._id)

    def machine_get_order(self, order):
        query = {"_id": self._id, "new_orders": {"$eq": order._id}}
        new_values = {
            "$pull": {"new_orders": order._id},
            "$addToSet": {"pending_orders": order._id}
        }
        updated_event = events.update_one(query, new_values)
        if updated_event.modified_count != 1:
            raise Order_Process_Exception
        self.new_orders.remove(order._id)
        self.pending_orders.append(order._id)

    def machine_finished_order(self, order):
        query = {"_id": self._id, "pending_orders": {"$eq": order._id}}
        new_values = {
            "$pull": {"pending_orders": order._id},
            "$addToSet": {"processed_orders": order._id}
        }
        updated_event = events.update_one(query, new_values)
        if updated_event.modified_count != 1:
            raise Order_Process_Exception
        self.pending_orders.remove(order._id)
        self.processed_orders.append(order._id)

    @classmethod
    def find(cls, event_key):
        _id = base64.urlsafe_b64decode(event_key.encode('utf8')).decode('utf8')
        found_event_data = events.find_one({"_id": ObjectId(_id)})
        if found_event_data is None:
            return None
        found_event = cls(found_event_data.get('name'),
                          found_event_data.get('location'))
        found_event.users = found_event_data.get('users')
        found_event.new_orders = found_event_data.get('new_orders')
        found_event.pending_orders = found_event_data.get('pending_orders')
        found_event.processed_orders = found_event_data.get('processed_orders')
        found_event.machines = found_event_data.get('machines')
        found_event._id = found_event_data.get('_id')
        return found_event

    @classmethod
    def find_all(cls):
        found_event_data = events.find({})
        if found_event_data is None:
            return None
        formated_event_data = []
        for event in found_event_data:
            found_event = cls(event.get('name'),
                              event.get('location'))
            found_event.users = event.get('users')
            found_event.new_orders = event.get('new_orders')
            found_event.pending_orders = event.get('pending_orders')
            found_event.processed_orders = event.get(
                'processed_orders')
            found_event.machines = event.get('machines')
            found_event._id = event.get('_id')
            formated_event_data.append(found_event.to_dict(everyfeild=True))
        return formated_event_data

    def to_dict(self, everyfeild=False):
        str_event_key = str(str(self._id)).encode('utf8')
        if everyfeild:
            return {
                'event_key': base64.urlsafe_b64encode(str_event_key).decode('utf8'),
                'name': self.name,
                'location': self.location,
                'users': [str(user) for user in self.users],
                'new_orders': [str(new_order) for new_order in self.new_orders],
                'pending_orders': [str(pending_order) for pending_order in self.pending_orders],
                'processed_orders': [str(processed_order) for processed_order in self.processed_orders],
                'machines': [str(machine) for machine in self.machines],
            }
        return {
            'event_key': base64.urlsafe_b64encode(str_event_key).decode('utf8'),
            'name': self.name,
            'location': self.location,
        }
