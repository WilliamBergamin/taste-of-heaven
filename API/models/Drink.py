
class Drink:

    """
    Attributes:
      swagger_types (dict): The key is attribute name
                            and the value is attribute type.
      attribute_map (dict): The key is attribute name
                            and the value an explanation of the attribute role.
    """
    swagger_types = {
        'mixer_type': 'str',
        'alcohol_type': 'str',
        'double': 'bool',
    }

    attribute_map = {
        'mixer_type': 'what mixer do you want in it',
        'alcohol_type': 'type of alcohol in the drink',
        'double': 'is it a double drink',
    }

    def __init__(self, mixer_type=None, alcohol_type=None, double=False):
        self.mixer_type = mixer_type
        self.alcohol_type = alcohol_type
        self.double = double
        self.price = 3.5

    def to_dict(self):
        return {
            'mixer_type': self.mixer_type,
            'alcohol_type': self.alcohol_type,
            'double': self.double,
            'price': self.price
        }
