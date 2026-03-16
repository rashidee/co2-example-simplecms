<?php

use Illuminate\Support\Facades\Route;
use Modules\Blog\Http\Controllers\BlogPageController;

Route::get('/blog', [BlogPageController::class, 'index'])->name('blog.index');
Route::get('/blog/{slug}', [BlogPageController::class, 'show'])->name('blog.show')->where('slug', '[a-z0-9\-]+');
