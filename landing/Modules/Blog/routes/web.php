<?php

use Illuminate\Support\Facades\Route;
use Modules\Blog\Http\Controllers\BlogController;

Route::get('/blog', [BlogController::class, 'directory'])->name('blog.directory');
Route::get('/{slug}', [BlogController::class, 'detail'])->name('blog.detail')->where('slug', '[a-z0-9\-]+');
