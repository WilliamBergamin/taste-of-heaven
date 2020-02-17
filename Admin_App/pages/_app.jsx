import React from 'react';
import App from 'next/app';
// eslint-disable-next-line no-unused-vars
import fetch from 'isomorphic-unfetch';
// import ApolloClient from 'apollo-boost';
import { ThemeProvider } from '@material-ui/styles';
import getConfig from 'next/config';
import theme from '../theme/Theme'


// const { publicRuntimeConfig } = getConfig();


class MyApp extends App {
  render() {
    const { Component, pageProps } = this.props;

    return (
      <ThemeProvider theme={theme}>
        <Component {...pageProps} />
      </ThemeProvider>
    );
  }
}


export default MyApp;
