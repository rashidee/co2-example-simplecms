<?php

use Illuminate\Support\Facades\Route;
use Modules\ContactSection\Http\Controllers\ContactFragmentController;

Route::post('/contact', [ContactFragmentController::class, 'store'])->name('contact.store');
