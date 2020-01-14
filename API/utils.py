from flask import make_response
import json

from init import app, JSON_MIME_TYPE


def json_response(data='', status=200, headers=None):
    headers = headers or {}
    if 'Content-Type' not in headers:
        headers['Content-Type'] = JSON_MIME_TYPE
    res = make_response(data, status, headers)
    return res


def json_error(log='', error_message='', status=400, headers=None):
    app.logger.info(log)
    if error_message == '':
        return json_response(status=status, headers=headers)
    error = json.dumps({'error': error_message})
    return json_response(error, status, headers)
