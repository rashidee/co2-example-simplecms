<?php

use Illuminate\Support\Facades\Route;
use Modules\ContactSection\Http\Controllers\ContactSectionController;

Route::post('/contact', [ContactSectionController::class, 'store'])->name('contact.store');
