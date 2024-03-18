<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Contacto extends Model
{
    use HasFactory;

    public $fillable = [
        'pais', 'nombre', 'cod_pais', 'telefono', 'nota', 'latitud', 'longitud', 'avatar', 'video', 'status', 'created_by', 'created_at'
    ];

}
