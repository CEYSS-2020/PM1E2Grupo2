<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Paise extends Model
{
    use HasFactory;

    public $fillable = [
        'codigo', 'pais', 'created_at', 'created_by'
    ];

}
