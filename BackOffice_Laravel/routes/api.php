<?php

use App\Http\Controllers\Api\APIContactosController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;


//Route::group(['middleware' => ['apikey']], function(){
    Route::get('allContactos', [APIContactosController::class, 'allContactos']);
    Route::get('contacto/{id}', [APIContactosController::class, 'ShowContacto']);

    Route::post('addContacto', [APIContactosController::class, 'addContacto']);
    Route::post('updateContacto', [APIContactosController::class, 'updateContacto']);
    Route::post('deleteContacto', [APIContactosController::class, 'deleteContacto']);
    Route::get('map', [APIContactosController::class, 'Map']);

//});


Route::group(['middleware' => ['auth:sanctum']], function(){
    Route::get('user-profile', [APIContactosController::class, 'userProfile']);
    Route::post('logout', [APIContactosController::class, 'logout']);

});
