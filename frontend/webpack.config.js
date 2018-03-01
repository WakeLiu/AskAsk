var webpack = require('webpack');
var path = require('path');
var ExtractTextPlugin = require('extract-text-webpack-plugin');

module.exports = {
    devtool: 'eval',
    entry: [
        './index.jsx'
    ],
    output: {
        path: path.join(__dirname, 'assets'),
        filename: 'bundle.js',
        publicPath: '/assets/'
    },
    module:{
        loaders: [{
            test: /\.jsx?$/,
            loaders: [
                'react-hot',
                'babel-loader',
                'babel?presets[]=es2015&presets[]=stage-0&presets[]=react'
            ],
            exclude: /node_modules/
        }, {
            test: /\.scss$/,
            loader: ExtractTextPlugin.extract('css?sourceMap!sass'),
        }]
    },
    plugins: [
        new ExtractTextPlugin('/main.css'),
        new webpack.HotModuleReplacementPlugin()
    ],
    devServer: {
        historyApiFallback: true,
        contentBase: './',
        hot: true,
        inline: true,
        port: 3000
    }
}
