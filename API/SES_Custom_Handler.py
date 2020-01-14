import logging
import boto3


class SES_Custom_Handler(logging.Handler):

    html_body = """<html>
    <head></head>
    <body style="background-color:rgb(255, 99, 71);">
        <h1 style="color:white" align="center">An Error Occured!</h1>
        <h2 style="color:white" align="center">Taste of Heaven API</h2>
        <pre style="color:white">{trace}</pre>
    </body>
    </html>"""

    def __init__(self, aws_key, aws_secret, ses_region, mail_sender,
                 mailing_list, mail_subject=''):
        logging.Handler.__init__(self)
        self.aws_key = aws_key
        self.aws_secret = aws_secret
        self.aws_region = ses_region
        self.mail_sender = mail_sender
        self.mailing_list = mailing_list
        self.mail_subject = mail_subject

    def emit(self, record):
        client = boto3.client('ses',
                              region_name=self.aws_region,
                              aws_access_key_id=self.aws_key,
                              aws_secret_access_key=self.aws_secret)
        client.send_email(Destination={'ToAddresses': self.mailing_list},
                          Message={
                            'Body': {
                                'Html': {
                                    'Data': self.html_body.format(
                                                trace=self.format(record)),
                                },
                            },
                            'Subject': {'Data': self.mail_subject},
                          },
                          Source=self.mail_sender)
