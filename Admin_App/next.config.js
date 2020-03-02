require('dotenv').config();

const path = require('path');
const Dotenv = require('dotenv-webpack');

module.exports = {
  webpack: (config, { isServer }) => {
    const Config = config;
    Config.plugins = config.plugins || [];

    Config.plugins = [
      ...config.plugins,

      // Read the .env file
      new Dotenv({
        path: path.join(__dirname, '.env'),
        systemvars: true,
      }),
    ];
    if (!isServer) {
      Config.node = {
        fs: 'empty',
      };
    }

    return Config;
  },
};
