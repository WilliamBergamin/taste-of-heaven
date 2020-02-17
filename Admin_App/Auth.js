import Router from 'next/router'
import fetch from 'isomorphic-unfetch';
import Cookies from 'universal-cookie';

const baseURL = "http://3.133.81.46:80/";

var cookies;

class Auth {

    constructor() {
        this.cookies = null;
        this.idToken = null;
        this.expiresAt = null;
    }

    initialise(req) {
        this.cookies = new Cookies(req.cookies);
        this.idToken = this.cookies.get('token');
        this.expiresAt = this.cookies.get('expiresAt');
    }

    login() {
        Router.push('/index')
    }

    getIdToken() {
        return this.idToken;
    }

    handleAuthentication(username, password) {
        return new Promise(async (resolve, reject) => {
            const response = await fetch(baseURL + '/api/v1/user/token', {
                method: 'POST',
                body: {
                    "email": username,
                    "password": password
                },
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            const responseJson = await response.json();
            if (!responseJson || !responseJson.token) {
                return reject();
            }
            this.setSession(responseJson);
            resolve();
        });
    }

    setSession(User) {
        this.idToken = User.token;
        this.expiresAt = 86400 * 1000 + new Date().getTime();
        this.cookies.set('token', this.idToken)
        this.cookies.set('expiresAt', this.expiresAt)
    }

    isAuthenticated() {
        // Check whether the current time is past the token's expiry time
        return new Date().getTime() < this.expiresAt;
    }
}

const auth = new Auth();

module.exports = auth;