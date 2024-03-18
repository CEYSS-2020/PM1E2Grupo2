@extends('layouts.main')
@section('title', 'Editar Contacto')
@section('breadcrumb')
    <div class="col-md-12">
        <div class="page-header-title">
            <h4 class="m-b-10">{{ __('Editar Contacto') }}</h4>
        </div>
        <ul class="breadcrumb">
            <li class="breadcrumb-item">{!! Html::link(route('home'), __('Dashboard'), []) !!}</li>
            <li class="breadcrumb-item"><a href="{{ route('contactos.index') }}">{{ __('Contactos') }}</a></li>
            <li class="breadcrumb-item active">{{ __('Editar Contacto') }}</li>
        </ul>
    </div>
@endsection
@push('script')
    <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"
        integrity="sha384-DfXdz2htPH0lsSSs5nCTpuj/zy4C+OGpamoFVy38MVBnE+IbbVYUew+OrCXaRkfj" crossorigin="anonymous">
    </script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
        integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous">
    </script>
    <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key={{ env('GOOGLE_MAP_KEY') }}"></script>
    <script>
        var vMarker
        var map
        map = new google.maps.Map(document.getElementById('map_canvas'), {
            zoom: 14,
            center: new google.maps.LatLng(14.062424, -87.182661),
            mapTypeId: google.maps.MapTypeId.ROADMAP
        });
        vMarker = new google.maps.Marker({
            position: new google.maps.LatLng(14.062424, -87.182661),
            draggable: true
        });
        google.maps.event.addListener(vMarker, 'dragend', function(evt) {
            $("#latitud").val(evt.latLng.lat().toFixed(6));
            $("#longitud").val(evt.latLng.lng().toFixed(6));

            map.panTo(evt.latLng);
        });
        map.setCenter(vMarker.position);
        vMarker.setMap(map);

        $("#txtCiudad, #txtEstado, #txtDireccion").change(function() {
            movePin();
        });

        function movePin() {
            var geocoder = new google.maps.Geocoder();
            var textSelectM = $("#txtCiudad").text();
            var textSelectE = $("#txtEstado").val();
            var inputAddress = $("#txtDireccion").val() + ' ' + textSelectM + ' ' + textSelectE;
            geocoder.geocode({
                "address": inputAddress
            }, function(results, status) {
                if (status == google.maps.GeocoderStatus.OK) {
                    vMarker.setPosition(new google.maps.LatLng(results[0].geometry.location.lat(), results[0]
                        .geometry.location.lng()));
                    map.panTo(new google.maps.LatLng(results[0].geometry.location.lat(), results[0].geometry
                        .location.lng()));
                    $("#latitud").val(results[0].geometry.location.lat());
                    $("#longitud").val(results[0].geometry.location.lng());
                }

            });
        }
    </script>
@endpush
@section('content')
    <div class="col-sm-8 m-auto">
        <div class="card">
            <div class="card-header">
                <h5>{{ __('Editar Contacto') }}</h5>
            </div>
            <div class="card-body">
                {!! Form::model($contacto, [
                    'route' => ['contactos.update', $contacto->id],
                    'method' => 'Patch',
                    'class' => 'form-horizontal',
                    'data-validate',
                    'enctype' => 'multipart/form-data',
                ]) !!}
                <div class="row">
                    <div class="col-sm-6">
                        <div class="form-group">
                            {{ Form::label('nombre', 'Nombre', ['class' => 'col-form-label']) }}
                            {!! Form::text('nombre', null, ['class' => 'form-control', 'required', 'placeholder' => __('Enter name')]) !!}
                        </div>

                        <div class="form-group  ">
                            {{ Form::label('nota', 'Nota', ['class' => 'col-form-label']) }}
                            {!! Form::textarea('nota', null, [
                                'class' => 'form-control',
                                "rows" => "5",
                                'required',
                                'placeholder' => __('Ingrese una Nota'),
                            ]) !!}
                        </div>

                    </div>

                    <div class="col-sm-6">
                        <div class="form-group  row">
                            <div class="col-sm-8">
                                {{ Form::label('cod_pais', __('Pais'), ['class' => 'col-form-label']) }}
                                {!! Form::select('cod_pais', $paises, null, ['class' => 'form-select', 'id' => 'cod_pais']) !!}
                            </div>
                            <div class="col-sm-4">
                                {{ Form::label('name', 'Estado', ['class' => 'col-form-label']) }}
                                <div class="col-md-2 form-check form-switch custom-switch-v1">
                                    <label class="custom-switch mt-2 float-right">
                                        <input name="status" data-onstyle="primary" class="form-check-input input-primary"
                                            type="checkbox" @if ($contacto->status == 1) checked="" @endif
 />


                                    </label>
                                </div>
                            </div>
                        </div>

                        <div class="form-group  ">
                            {{ Form::label('telefono', 'Telefono', ['class' => 'col-form-label']) }}
                            {!! Form::text('telefono', null, [
                                'class' => 'form-control',
                                'required',
                                'placeholder' => __('95525921'),
                            ]) !!}
                        </div>

                        <div class="form-group">
                            <label for="avatar" class="form-label">{{ __('Avatar') }} *</label>
                            <div class="d-flex align-items-center">
                                <img src="{{ Storage::url($contacto->avatar) }}" width="50" class="mr-2" />
                                {!! Form::file('avatar', ['class' => 'form-control ml-4', 'id' => 'avatar']) !!}
                            </div>
                        </div>

                    </div>
                    <div class="form-group" id="map_canvas" style="height:350px">
                    </div>
                    <div class="row">
                        <div class="form-group  col-md-6">
                            {{ Form::label('latitud', 'Latitud', ['class' => 'col-form-label']) }}
                            {!! Form::text('latitud', null, [
                                'class' => 'form-control',
                                'id' => 'latitud',
                                'required',
                                'placeholder' => __('12345678'),
                            ]) !!}
                        </div>
                        <div class="form-group  col-md-6">
                            {{ Form::label('longitud', 'Longitud', ['class' => 'col-form-label']) }}
                            {!! Form::text('longitud', null, [
                                'class' => 'form-control',
                                'id' => 'longitud',
                                'required',
                                'placeholder' => __('12345678'),
                            ]) !!}
                        </div>

                    </div>
                </div>
            </div>
        </div>
        <div class="card-footer">
            <div class="text-end">
                <a href="{{ route('contactos.index') }}" class="btn btn-secondary">{{ __('Cancel') }}</a>
                {{ Form::button(__('Save'), ['type' => 'submit', 'class' => 'btn btn-primary']) }}
            </div>
            {!! Form::close() !!}
        </div>
    </div>
    </div>
@endsection
@push('script')
    <script src="{{ asset('assets/js/plugins/choices.min.js') }}"></script>
    </script>
@endpush
