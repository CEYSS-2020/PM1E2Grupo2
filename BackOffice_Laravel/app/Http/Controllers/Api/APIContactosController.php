<?php

namespace App\Http\Controllers\Api;

use Carbon\Carbon;
use App\Http\Controllers\Controller;
use App\Models\Contacto;
use Illuminate\Http\Request;
use Symfony\Component\HttpFoundation\Response;
use Illuminate\Support\Facades\Validator;
use Illuminate\Validation\Rule;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Http;
use App\Facades\Utility;
use DB;
use App\Facades\UtilityFacades;
use Hash;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Storage;

class APIContactosController extends Controller
{


    public function allContactos() {
        $coordenadas  = Contacto::where('status', 1)->get(['id', 'pais', 'nombre', 'telefono', 'nota',
        'latitud','longitud','avatar','video', 'status','created_at']);

        $coordenadasArray = [];

        foreach ($coordenadas as $coordenada) {
            $coordenadasArray[] = [
                'id' => $coordenada->id,
                'codc' => $coordenada->id,
                'pais' => $coordenada->pais,
                'nombre' => $coordenada->nombre,
                'telefono' => $coordenada->telefono,
                'nota' => $coordenada->nota,
                'latitud' => $coordenada->latitud,
                'longitud' => $coordenada->longitud,
                'avatar' => Storage::url($coordenada->avatar),
                'video' => Storage::url($coordenada->video),
                'ubicacion' => $coordenada->latitud.', '.$coordenada->longitud,
                'created_at' => Carbon::parse($coordenada->created_at)->format('m/d/Y'),
            ];
        }

        $response["status"] = "ok";
        $response["listacontactos"] = json_encode($coordenadasArray);
    	$response["message"] = '';

        return response($response,Response::HTTP_OK);

    }

    public function Map() {
        $coordenadas  = Contacto::where('status', 1)->get(['id', 'avatar', 'nombre', 'latitud', 'longitud','status']);

        $coordenadasArray = [];

        foreach ($coordenadas as $coordenada) {
            $coordenadasArray[] = [
                'id' => $coordenada->id,
                'avatar' => Storage::url($coordenada->avatar),
                'nombre' => $coordenada->nombre,
                'ubicacion' => $coordenada->latitud.', '.$coordenada->longitud
            ];
        }

        return response()->json($coordenadasArray);
    }

    public function ShowContacto($id) {
        $coordenadas  = Contacto::where('id', $id)->get(['id', 'pais', 'nombre', 'telefono', 'nota',
        'latitud','longitud','avatar','video', 'status','created_at']);

        $coordenadasArray = [];

        foreach ($coordenadas as $coordenada) {
            $coordenadasArray[] = [
                'id' => $coordenada->id,
                'pais' => $coordenada->pais,
                'nombre' => $coordenada->nombre,
                'telefono' => $coordenada->telefono,
                'nota' => $coordenada->nota,
                'latitud' => $coordenada->latitud,
                'longitud' => $coordenada->longitud,
                'avatar' => Storage::url($coordenada->avatar),
                'video' => Storage::url($coordenada->video),
                'ubicacion' => $coordenada->latitud.', '.$coordenada->longitud,
                'created_at' => Carbon::parse($coordenada->created_at)->format('m/d/Y'),
            ];
        }

        return response()->json($coordenadasArray);

    }



    public function addContacto(Request $request)
    {

        $rules = [
            'pais' => 'required',
            'nombre' => 'required',
            'telefono' => 'required',
            'avatar' => 'required',
        ];

        $validator = Validator::make($request->all(), $rules);
        if ($validator->fails()) {
            $messages = $validator->getMessageBag();
            return response(["response"=> $messages->first() ],Response::HTTP_UNAUTHORIZED);
        }

        $disk = Storage::disk();
        $image = $request->avatar;
        $image = str_replace('data:image/png;base64,', '', $image);
        $image = str_replace(' ', '+', $image);
        $imagename = time() . '.' . 'png';
        $imagepath = "contactos/" . $imagename;
        $disk->put($imagepath, base64_decode($image));

        $input = $request->all();
        $input['status'] = 1;
        $input['avatar'] = $imagepath;
        $user = Contacto::create($input);

        $response["status"] = "ok";
    	$response["message"] = 'Contacto registrado con exito.';

        return response($response,Response::HTTP_OK);


    }

    public function updateContacto(Request $request)
    {
        $rules = [
            'pais' => 'required',
            'nombre' => 'required',
            'telefono' => 'required',
            'avatar' => 'required',
        ];

        $validator = Validator::make($request->all(), $rules);
        if ($validator->fails()) {
            $messages = $validator->getMessageBag();
            return response(["response"=> $messages->first() ],Response::HTTP_UNAUTHORIZED);
        }

        $input = $request->all();
        $id = $request->id;


        $contacto = Contacto::find($id);

        if ($request->avatar == "Vacio") {
        }else{
            $disk = Storage::disk();
            $image = $request->avatar;
            $image = str_replace('data:image/png;base64,', '', $image);
            $image = str_replace(' ', '+', $image);
            $imagename = time() . '.' . 'png';
            $imagepath = "contactos/" . $imagename;
            $disk->put($imagepath, base64_decode($image));

            $contacto->avatar = $imagepath;

        }

        $contacto->nombre = $request->nombre;
        $contacto->telefono = $request->telefono;
        $contacto->pais = $request->pais;
        $contacto->nota = $request->nota;
        $contacto->latitud = $request->latitud;
        $contacto->longitud = $request->longitud;
        $contacto->video = $request->video;
        $contacto->save();

        $response["status"] = "ok";
    	$response["message"] = 'Contacto actualizado con exito.';

        return response($response,Response::HTTP_OK);


    }

    public function deleteContacto(Request $request)
    {
            $id = $request->id;

            $contacto = Contacto::find($id);
            if ($contacto) {
                $contacto->delete();

                $response["status"] = "ok";
            	$response["message"] = 'Contacto eliminado con éxito.';
    			return response($response,Response::HTTP_OK);
            } else {
                $response["status"] = "ok";
            	$response["message"] = 'El contacto con el ID especificado no existe.';
            	return response($response,Response::HTTP_NOT_FOUND);
            }


    }



}
