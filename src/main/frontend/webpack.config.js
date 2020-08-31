const path = require('path');
const merge = require('webpack-merge');
const webpack = require('webpack');
const os = require('os');
const WRMPlugin = require('atlassian-webresource-webpack-plugin');
const providedDependencies = require('./providedDependencies');

const PLUGIN_TARGET_DIR = path.join(__dirname, '..', '..', '..', 'target');
const OUTPUT_DIR = path.join(PLUGIN_TARGET_DIR, 'classes');
const SRC_DIR = path.join(__dirname, 'src');

const getWrmPlugin = (watch = false, watchPrepare = false) => {
  return new WRMPlugin({
    pluginKey: 'com.ntnguyen.app.confluence.license-tracking-app',
    webresourceKeyMap: {
      // Take the entry "mltMacroEntry" and create a resource with key "mlt-macro-resource"
      'mltMacroEntry': 'mlt-macro-resource',
      'mltConfigEntry': 'mlt-config-resource',
    },
    xmlDescriptors: path.join(OUTPUT_DIR, 'META-INF', 'plugin-descriptors',
      'wr-webpack-bundles.xml'),
    providedDependencies: providedDependencies,
    watch: watch,
    watchPrepare: watchPrepare,
  });
};

const webpackConfig = {
  entry: {
    mltMacroEntry: path.join(SRC_DIR, 'entry/mlt-macro.entry.js'),
    mltConfigEntry: path.join(SRC_DIR, 'entry/mlt-config.entry.js'),
  },
  output: {
    filename: '[name].[chunkhash].js',
    path: OUTPUT_DIR,
    chunkFilename: '[name].[chunkhash].js',
  },
  mode: 'development',
  module: {
    rules: [
      {
        test: /\.css$/,
        use: [
          'style-loader',
          'css-loader',
        ],
      },
      {
        test: /\.(png|svg|jpg|gif|woff|woff2|eot|ttf|otf)$/,
        use: [
          'file-loader',
        ],
      },
      {
        // Only pack file js or jsx (test the file)
        test: /\.(js|jsx)$/,
        exclude: /node_modules/,
        // User babel loader. Loader can be considered as a bridge between babel and Webpack
        use: [
          'babel-loader',
        ],
      },
    ],
  },
  optimization: {
    splitChunks: false,
    runtimeChunk: false,
  },
  devtool: 'cheap-module-source-map',
  resolve: {
    modules: [
      'node_modules',
      SRC_DIR,
    ],
  },
  plugins: [new webpack.NamedChunksPlugin()],
};

const hostname = os.hostname();
const devServerPort = '3333';

const watchPrepareConfig = {
  output: {
    publicPath: `http://${hostname}:${devServerPort}/`,
    filename: '[name].js',
    chunkFilename: '[name].chunk.js',
  },
  plugins: [
    getWrmPlugin(true, true),
  ],
};

const watchConfig = {
  output: {
    publicPath: `http://${hostname}:${devServerPort}/`,
    filename: '[name].js',
    chunkFilename: '[name].chunk.js',
  },
  devServer: {
    host: hostname,
    port: devServerPort,
    overlay: true,
    hot: true,
    headers: { 'Access-Control-Allow-Origin': '*' },
  },
  plugins: [
    new webpack.NamedModulesPlugin(),
    new webpack.HotModuleReplacementPlugin(),
    getWrmPlugin(true),
  ],
};

const devConfig = {
  optimization: {
    splitChunks: {
      minSize: 0,
      chunks: 'all',
      maxInitialRequests: Infinity,
    },
    runtimeChunk: true,
  },
  plugins: [
    getWrmPlugin(),
  ],
};

module.exports = (env) => {
  if (env === 'watch:prepare') {
    return merge([webpackConfig, watchPrepareConfig]);
  }

  if (env === 'watch') {
    return merge([webpackConfig, watchConfig, watchPrepareConfig]);
  }

  return merge([webpackConfig, devConfig]);
};
