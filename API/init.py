import os
import logging
from pymongo import MongoClient
from flask import Flask
from logging.handlers import RotatingFileHandler
from SES_Custom_Handler import SES_Custom_Handler

true_list = ['true', '1', 't', 'y', 'yes']

mongodb_host = os.environ.get('MONGODB_HOST', 'localhost')
mongodb_port = os.environ.get('MONGODB_PORT', '27017')

if not os.path.exists('./logs'):
    os.mkdir('./logs')

file_log_handler = RotatingFileHandler(
                            './logs/error.log',
                            maxBytes=100000000,
                            backupCount=5)
file_log_handler.setFormatter(
    logging.Formatter(
        '%(asctime)s %(levelname)s: %(message)s [in %(pathname)s:%(lineno)d]'
        )
    )
file_log_handler.setLevel(logging.INFO)

app = Flask(__name__)

app.logger.addHandler(file_log_handler)
if os.environ.get('EMAIL_ERROR_ENABLED', 'false').lower() in true_list:
    number = 0
    mailing_list = []
    while(os.environ.get('RECIVER_EMAIL_'+str(number), None) is not None):
        mailing_list.append(os.environ.get('RECIVER_EMAIL_'+str(number)))
        number += 1
    mail_log_handler = SES_Custom_Handler(
        aws_key=os.environ.get('AWS_SES_KEY'),
        aws_secret=os.environ.get('AWS_SES_SECRET'),
        ses_region=os.environ.get('AWS_SES_REGION'),
        mail_sender=os.environ.get('SOURCE_EMAIL'),
        mailing_list=mailing_list,
        mail_subject='ERROR in Taste of Heaven API'
    )
    mail_log_handler.setLevel(logging.ERROR)
    app.logger.addHandler(mail_log_handler)

app.logger.setLevel(logging.INFO)
app.logger.info('application started')
app.logger.info(str(mailing_list))

client = MongoClient('mongodb://'+mongodb_host+':'+mongodb_port+'/',
                     connect=False)
mongo_db = client['taste_of_heaven']

JSON_MIME_TYPE = 'application/json'
