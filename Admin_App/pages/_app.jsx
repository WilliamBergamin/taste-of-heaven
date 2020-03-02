import React from 'react';
import App from 'next/app';
import { ThemeProvider } from '@material-ui/styles';
import theme from '../theme/Theme'


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
