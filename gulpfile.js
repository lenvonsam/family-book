var gulp = require('gulp'),
  sass = require('gulp-sass'),
  autoprefixer = require('gulp-autoprefixer'),
  minifycss = require('gulp-minify-css'),
  rename = require('gulp-rename'),
  shell = require('gulp-shell');
  livereload = require('gulp-livereload');

livereload({
  start:true,
  port:33398
})
var staticRoot = 'src/main/resources/static/';

gulp.task('styles', function() {
  gulp.src(staticRoot + 'sass/*.scss')
    .pipe(sass({
      style: 'expanded',
      "sourcemap=none": true
    }))
    .pipe(autoprefixer('last 2 version', 'safari 5', 'ie 8', 'ie 9', 'opera 12.1'))
    .pipe(gulp.dest(staticRoot + 'css'))
    .pipe(rename({
      suffix: '.min'
    }))
    .pipe(minifycss())
    .pipe(gulp.dest(staticRoot + 'css'));
});

gulp.task('compile', shell.task([
  'gradle bootRepackage'
]));

//FIXME 有时间修改liveReload
// gulp.task('compileLiveReload',function(){
//   gulp.src('./src/**')
//     .pipe(shell(['gradle compileJava','gradle compileKotlin']))
//     .pipe(livereload());
// });

gulp.task('watch', function() {
  // gulp.watch(staticRoot + 'sass/**', ['styles', 'compile']);
  livereload.listen();
  gulp.watch(['./src/**'], ['compile']);
});
gulp.task('default', ['watch'], function() {});
