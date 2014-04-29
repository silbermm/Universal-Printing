module.exports = function(grunt) {
  grunt.
    initConfig({
      pkg : grunt.file.readJSON('package.json'),
      html2js: {
        options: {
          rename : function (moduleName) {
            return  moduleName.replace('app/dev/js/', '');
          },
          htmlmin: { 
            collapseBooleanAttributes: true,
            collapseWhitespace: true,
            removeAttributeQuotes: true,
            removeComments: true,
            removeEmptyAttributes: true,
            removeRedundantAttributes: true,
            removeScriptTypeAttributes: true,
            removeStyleLinkTypeAttributes: true
          }
        },
        main: {
          src: ['app/dev/js/**/*.tpl.html'],
          dest: 'public/js/templates.js'
        },
      },
      concat: {
        basic_and_extras: {
          files: {
            "public/js/common.js" : [
              'bower_components/jquery/dist/jquery.js',
              'bower_components/bootstrap/dist/js/bootstrap.js',
              'bower_components/angular/angular.js',
              'bower_components/angular-ui-bootstrap-bower/ui-bootstrap-tpls.js',
              'bower_components/angular-ui-router/release/angular-ui-router.js',
              'app/dev/js/app.js',
              'app/dev/js/**/*.js',
            ],
          },
        },
      },
      uglify : {
        options : {
          banner : '/*! <%= pkg.name %> <%= grunt.template.today("yyyy-mm-dd") %> */\n',
            compress : {
              global_defs : {
                "DEBUG" : false
              },
              dead_code : true
            }
        },
        build : {
          files : {
            "public/js/common.min.js" : [
              "public/js/common.js",
              "public/js/templates.js"
             ],
          }
        }
      },
      less : {
        development : {
          options : {
            paths : [
              "app/dev/less/",
              "bower_components/bootstrap/less/",
              "bower_components/font-awesome/less/"
            ]
          },
          files : {
            "app/dev/css/main.css" : "app/dev/less/main.less"
          }
        },
      },
      copy : {
        main: {
              files: [
                 {expand: true, flatten: true, src: ['bower_components/font-awesome/fonts/**'], dest: 'public/fonts/'},
                ]
        }
      },
      imagemin: {
        dynamic: {
          files: [{
            expand: true,
            cwd: 'app/dev/img/',
            src: ['**/*.{png,jpg,gif,ico}'],
            dest: 'public/img/'
          }]
        }
      },
      cssmin: {
        combine: {
          files: {
            'public/css/main.css': [
              'app/dev/css/main.css'
            ],
          }
        }
      },
      watch : {
        scripts : {
          files : [ 
            'app/dev/js/*.js',
            'app/dev/js/**/*.js',
            'app/dev/js/**/*.tpl.html',
            'less/*.less',
            'Gruntfile.js'
           ],
          tasks : [ 'default' ],
          options : {
            spawn : false,
          },
        },
      }
    });

    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-less');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-cssmin');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-html2js');
    grunt.loadNpmTasks('grunt-contrib-imagemin');
    grunt.registerTask('default', [ 'html2js','concat','uglify', 'less', 'copy','imagemin','cssmin' ]);
};
