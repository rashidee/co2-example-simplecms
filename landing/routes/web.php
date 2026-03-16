<?php

use App\Http\Controllers\HomeController;
use App\Http\Controllers\ImageController;
use Illuminate\Support\Facades\Route;

// Home page (composes all sections)
Route::get('/', [HomeController::class, 'index'])->name('home');

// Image serving endpoint (BLOB)
Route::get('/images/{table}/{id}/{column}', [ImageController::class, 'show'])
    ->name('image.show')
    ->where('table', 'hero-section|product-service|team-member|blog-post')
    ->where('column', 'image_data|thumbnail_data');
