<?php

namespace App\Http\Controllers;

use App\DataTables\ContactosDataTable;
use App\Models\Contacto;
use App\Models\Paise;
use Illuminate\Http\Request;

class ContactoController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index(ContactosDataTable $dataTable)
    {
        return $dataTable->render('contactos.index');

    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create()
    {
        $paises = Paise::pluck('pais', 'codigo');
        return view('contactos.create', compact('paises'));

    }


    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
        $this->validate($request, [
            'nombre' => 'required',
            'telefono' => 'required',
            'avatar' => 'required',
            'cod_pais' => 'required',
            'latitud' => 'required',
            'longitud' => 'required',
        ]);
        if ($request->hasFile('avatar')) {
            $request->validate([
                'avatar' => 'required',
            ]);
            $path = $request->file('avatar')->store('contactos');
        }
        $input = $request->all();
        $input['status'] = ($request->status == 'on') ? 1 : 0;
        $input['avatar'] = $path;
        $input['pais'] = $request->cod_pais;;
        $input['created_by'] = \Auth::user()->id;
        $contactos = Contacto::create($input);
        return redirect()->route('contactos.index')->with('success', __('Contacto creado con exito.'));

    }

    /**
     * Show the form for editing the specified resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function edit($id)
    {
            $paises = Paise::pluck('pais', 'codigo');
            $contacto = Contacto::find($id);
            return view('contactos.edit', compact('contacto', 'paises'));

    }


    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\Models\Contacto
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, $id)
    {
        $this->validate($request, [
            'nombre' => 'required',
            'telefono' => 'required',
            'cod_pais' => 'required',
        ]);
        $contacto = Contacto::find($id);
        $input = $request->all();
        if ($request->hasFile('avatar')) {
            $request->validate([
                'avatar' => 'required',
            ]);
            $path = $request->file('avatar')->store('contactos');

            $contacto->avatar = $path;

        }

        $contacto->nombre = $request->nombre;
        $contacto->telefono = $request->telefono;
        $contacto->pais = $request->cod_pais;
        $contacto->nota = $request->nota;
        $contacto->latitud = $request->latitud;
        $contacto->longitud = $request->longitud;
        $contacto->status = ($request->status == 'on') ? 1 : 0;
        $contacto->created_by = \Auth::user()->id;
        $contacto->save();

        return redirect()->route('contactos.index')->with('success', __('Contacto actualizado con exito.'));

    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\Models\Contacto
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
            $post = Contacto::find($id);
            $post->delete();
            return redirect()->route('contactos.index')->with('success', __('Contacto eliminado con exito.'));

    }

}

