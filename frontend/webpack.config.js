const HtmlWebpackPlugin = require('html-webpack-plugin');
const path = require('path');

const outputPath = path.join(__dirname, 'dist');

module.exports = {
    output: {
        path: outputPath,
        filename: 'index_bundle.js'
    },
    resolve: {
        extensions: ['.js', '.elm'],
        modules: ['node_modules']
    },
    entry: [
        `webpack-dev-server/client?http://localhost:9000`,
        path.join(__dirname, 'static/index.js')
    ],
    devServer: {
        historyApiFallback: true,
        contentBase: './src',
        hot: true,
        port: 9000,
        proxy: {
            "/": {
                target: `http://localhost:8080`,
                changeOrigin: true
            }
        }
    },
    module: {
        rules: [{
            test: /\.elm$/,
            exclude: [/elm-stuff/, /node_modules/],
            use: [{
                loader: 'elm-webpack-loader',
                options: {
                    verbose: true,
                    debug: true,
                    files: [
                        path.resolve(__dirname, 'src/Main.elm')
                    ]
                }
            }]
        },
        {
            test: /\.s[ac]ss$/,
            use: [
                'style-loader',
                'css-loader',
                'sass-loader'
            ]
        }]
    },
    plugins: [
        new HtmlWebpackPlugin({
            template: 'static/index.html',
            inject: 'body',
            filename: 'index.html'
        })
    ]
};