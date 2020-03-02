import json
from flask import g
from flask import request
from flask_cors import CORS
from flask_httpauth import HTTPTokenAuth

from models.Event import Event
from models.Event import Order_Process_Exception
from models.User import User, User_Creation_Exception
from models.Machine import Machine, Machine_Creation_Exception
from models.Order import Order
from models.Drink import Drink


from init import app, JSON_MIME_TYPE
from utils import json_response, json_error

auth = HTTPTokenAuth(scheme='Token')


@auth.verify_token
def verify_token(token):
    if token is None:
        return False
    if token[:7] == "machine":
        current_machine = Machine.find_by_token(token)
        if current_machine:
            g.current_machine = current_machine
            return True
        return False
    current_user = User.find_by_token(token)
    if current_user:
        g.current_user = current_user
        return True
    return False


@app.route('/', methods=['GET'])
def test():
    """
    Gets informaation about the application.
    :return: Information about this application.
    :rtype: str
    """
    return 'Taste of Heaven project up and running!'

# ---------------------- EVENT END-POINTS ---------------------- #
@app.route('/api/v1/event', methods=['POST'])
@auth.login_required
def post_event():
    """
    header Authorization: Token Authentication_user_token
    {
      "name":"your moms a hoe",
      "location":"ur moms house"
    }
    """
    if g.get('current_user', None) is None:
        return json_error('No user found might have been a machine token',
                          status=401)
    if g.current_user.admin is False:
        return json_error('User '+g.current_user._id+' cannot perform this action',
                          status=401)
    if request.content_type != JSON_MIME_TYPE:
        return json_error('Invalid Content Type', 'Invalid Content Type', 400)
    data = request.json
    new_event = Event(name=data['name'], location=data['location'])
    new_event.create()
    return json_response(json.dumps(new_event.to_dict()), status=201)


@app.route('/api/v1/events', methods=['GET'])
@auth.login_required
def get_events():
    """
    header Authorization: Token Authentication_user_token
    """
    if g.get('current_user', None) is None:
        return json_error('No user found might have been a machine token',
                          status=401)
    if g.current_user.admin is False:
        return json_error('User '+g.current_user._id+' cannot perform this action',
                          status=401)
    all_events = Event.find_all()
    if all_events is None:
        return json_error('Event not found', 'Event not found', 404)
    return json_response(json.dumps(all_events), status=201)


@app.route('/api/v1/event/<string:event_key>', methods=['GET'])
@auth.login_required
def get_event(event_key):
    """
    header Authorization: Token Authentication_user_token
    {
      "name":"your moms a hoe",
      "location":"ur moms house"
    }
    """
    if g.get('current_user', None) is None:
        return json_error('No user found might have been a machine token',
                          status=401)
    if g.current_user.admin is False:
        return json_error('User '+g.current_user._id+' cannot perform this action',
                          status=401)
    event = Event.find(event_key)
    if event is None:
        return json_error('Event not found', 'Event not found', 404)
    return json_response(json.dumps(event.to_dict(True)), status=201)

# ---------------------- USER END-POINTS ---------------------- #
@app.route('/api/v1/user', methods=['POST'])
def post_user():
    """
    {
      "name":"your moms a hoe",
      "email":"cool@gmail.com",
      "password":"ur moms house"
    }
    """
    if request.content_type != JSON_MIME_TYPE:
        return json_error('Invalid Content Type', 'Invalid Content Type', 400)
    data = request.json
    new_user = User(name=data['name'],
                    email=data['email'],
                    password=data['password'])
    try:
        new_user.create()
    except User_Creation_Exception:
        return json_error('Email already used for existing user',
                          'Email already used for existing user', status=409)
    return json_response(json.dumps(new_user.to_dict()), status=201)


@app.route('/api/v1/user/token', methods=['GET', 'POST'])
def get_token():
    """
    {
      "email":"cool@gmail.com",
      "password":"ur moms house"
    }
    """
    if request.content_type != JSON_MIME_TYPE:
        return json_error('Invalid Content Type', 'Invalid Content Type', 400)
    data = request.json
    user = User.find(data.get('email', None))
    if user is None:
        return json_error('User was not founc with given email',
                          'The authentification failed', status=401)
    user.get_token(data.get("password", None))
    user.save()
    if user.token is None:
        return json_error('The authentification failed',
                          'The authentification failed', status=401)
    return json_response(json.dumps(user.to_dict()), status=200)


@app.route('/api/v1/user', methods=['GET'])
@auth.login_required
def get_user():
    """
    header Authorization: Token Authentication_user_token
    """
    if g.get('current_user', None) is None:
        return json_error('No user found might have been a machine token',
                          status=401)
    return json_response(json.dumps(g.current_user.to_dict()), status=200)


@app.route('/api/v1/user/event/<string:event_key>', methods=['POST'])
@auth.login_required
def post_user_to_event(event_key):
    """
    header Authorization: Token Authentication_user_token
    /api/v1/user/event/<string:event_key>
    """
    if g.get('current_user', None) is None:
        return json_error('No user found might have been a machine token',
                          status=401)
    event = Event.find(event_key)
    if event is None:
        return json_error('Event not found', 'Event not found', 404)
    event.add_user(g.current_user)
    return json_response(json.dumps(event.to_dict()), status=200)


@app.route('/api/v1/order', methods=['POST'])
@auth.login_required
def post_order():
    """
    header Authorization: Token Authentication_user_token
    {
      "event_key": "y37jsnks",
      "drinks": [{
          "mixer_type": "coke",
          "alcohol_type": "rhum",
          "double": True
      }],
    }
    """
    if request.content_type != JSON_MIME_TYPE:
        return json_error('Invalid Content Type', 'Invalid Content Type', 400)
    if g.get('current_user', None) is None:
        return json_error('No user found might have been a machine token',
                          status=401)
    data = request.json
    event = Event.find(data.get('event_key'))
    if event is None:
        return json_error('Event not found', 'Event not found', 404)
    drinks = [Drink(drink.get('mixer_type'),
                    drink.get('alcohol_type'),
                    drink.get('double')) for drink in data.get('drinks')]
    # total_price = Order.get_price_from_drinks(drinks)
    # TODO process transation, for now assume the trasaction always passes
    new_order = Order(g.current_user._id, drinks, payed=True)
    new_order.create()
    event.add_new_order(new_order)
    g.current_user.add_order(new_order)
    return json_response(json.dumps(new_order.to_dict()), status=200)

# ---------------------- MACHINE END-POINTS ---------------------- #
@app.route('/api/v1/machine/<string:event_key>', methods=['POST'])
@auth.login_required
def post_machine(event_key):
    """
    header Authorization: Token Authentication_user_token
    """
    if g.get('current_user', None) is None:
        return json_error('No user found might have been a machine token',
                          status=401)
    new_machine = Machine()
    try:
        new_machine.create(g.current_user)
    except Machine_Creation_Exception:
        return json_error('User: '+str(g.current_user._id)+' unautherised for this creation action',
                          status=401)
    event = Event.find(event_key)
    if event is None:
        return json_error('Event not found', 'Event not found', 404)
    event.add_machine(new_machine)
    return json_response(json.dumps(new_machine.to_dict()), status=200)


@app.route('/api/v1/machine/<string:machine_key>', methods=['GET'])
@auth.login_required
def get_machine(machine_key):
    """
    header Authorization: Token Authentication_user_token
    """
    if g.get('current_user', None) is None:
        return json_error('No user found might have been a machine token',
                          status=401)
    if g.current_user.admin is False:
        return json_error('User '+g.current_user._id+' cannot perform this action',
                          status=401)
    found_machine = Machine.find(machine_key)
    if found_machine is None:
        return json_error('Machine not found', 'Machine not found', 404)
    return json_response(json.dumps(found_machine.to_dict(withToken=False)), status=200)


# @app.route('/api/v1/machine/event/<string:event_key>', methods=['POST'])
# @auth.login_required
# def post_machine_to_event(event_key):
#     """
#     header Authorization: Token Authentication_machine_token
#     /api/v1/user/event/<string:event_key>
#     """
#     if g.get('current_machine', None) is None:
#         return json_error('No machine found might have been a user token', status=401)
#     event = Event.find(event_key)
#     if event is None:
#         return json_error('Event not found', 'Event not found', 404)
#     event.add_machine(g.current_machine)
#     return json_response(json.dumps(event.to_dict()), status=200)


@app.route('/api/v1/machine/order/<string:event_key>/<string:order_key>', methods=['GET', 'POST'])
@auth.login_required
def machine_get_order(event_key, order_key):
    """
    header Authorization: Token Authentication_machine_token
    """
    if g.get('current_machine', None) is None:
        return json_error('No machine found might have been a user token', status=401)
    if g.current_machine.selected_order is not None:
        return json_error('Machine has not finished its selected order',
                          'Machine not finished selected order', 403)
    order = Order.find(order_key)
    if order is None:
        return json_error('Order not found', 'Order not found', 404)
    if order.machine_id is not None:
        return json_error('Order already has an associated machine',
                          'Order already has an associated machine', 403)
    event = Event.find(event_key)
    if event is None:
        return json_error('Event not found', 'Event not found', 404)
    try:
        event.machine_get_order(order)
    except Order_Process_Exception:
        return json_error('Order: '+str(order._id)+' was not found in the new_orders of event: '+str(event._id),
                          'Order was not found in event new order', 404)
    g.current_machine.set_selected_order(order)
    order.assigne_machine(g.current_machine._id)
    return json_response(json.dumps(order.to_dict()), status=200)


@app.route('/api/v1/machine/order/done/<string:event_key>', methods=['POST'])
@auth.login_required
def machine_post_order_completed(event_key):
    """
    header Authorization: Token Authentication_machine_token
    """
    if g.get('current_machine', None) is None:
        return json_error('No machine found might have been a user token', status=401)
    if g.current_machine.selected_order is None:
        return json_error('Machine has no order to finsih',
                          'Machine has no order to finish', 403)
    order = Order.find_by_id(g.current_machine.selected_order)
    if order is None:
        return json_error('Order not found', 'Order not found', 404)
    if order.machine_id is None:
        return json_error('Order does not have machine id, it should have one',
                          'Order does not have machine_id', 403)
    event = Event.find(event_key)
    if event is None:
        return json_error('Event not found', 'Event not found', 404)
    try:
        event.machine_finished_order(order)
    except Order_Process_Exception:
        return json_error('Order: '+str(order._id)+' was not found in the new_orders of event: '+str(event._id),
                          'Order was not found in event new order', 404)
    g.current_machine.set_selected_order_done()
    order.set_done()
    return json_response(json.dumps(order.to_dict()), status=200)


CORS(app)
