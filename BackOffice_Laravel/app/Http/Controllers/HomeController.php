<?php

namespace App\Http\Controllers;

use App\Facades\UtilityFacades;
use App\Models\Role;
use App\Models\User;
use Hashids\Hashids;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use App\Models\Contacto;

class HomeController extends Controller
{
    public function __construct()
    {
        // $this->middleware(['auth', '2fa']);
    }

    public function landingPage($lang = 'en')
    {

        return redirect()->route('login');

    }

    public function index()
    {
        $usr = \Auth::user();
        $user_id = $usr->id;
        $roles = Role::where('name', $usr->type)->first();
        $role_id = $usr->roles->first()->id;
        $user = User::count();
        $contactos = Contacto::count();

        return  view('dashboard/home', compact('user', 'contactos'));

    }

    public function changeThememode(Request $request)
    {
        $user = \Auth::user();
        if ($user->dark_layout == 1) {
            $user->dark_layout = 0;
        } else {
            $user->dark_layout = 1;
        }
        $user->save();
        return response()->json(['mode' => $user->dark_layout]);
    }


    public function read_notification()
    {
        auth()->user()->notifications->markAsRead();
        return response()->json(['is_success' => true], 200);
    }

}
