@php
    $user_theme = \Auth::user();
    $color = $user_theme->theme_color;
    $chatcolor = '#145388';
    if ($color == 'theme-1') {
        $chatcolor = '#0CAF60';
    } elseif ($color == 'theme-2') {
        $chatcolor = '#584ED2';
    } elseif ($color == 'theme-3') {
        $chatcolor = '#6FD943';
    } elseif ($color == 'theme-4') {
        $chatcolor = '#145388';
    } elseif ($color == 'theme-5') {
        $chatcolor = '#B9406B';
    } elseif ($color == 'theme-6') {
        $chatcolor = '#008ECC';
    } elseif ($color == 'theme-7') {
        $chatcolor = '#922C88';
    } elseif ($color == 'theme-8') {
        $chatcolor = '#C0A145';
    } elseif ($color == 'theme-9') {
        $chatcolor = '#48494B';
    } elseif ($color == 'theme-10') {
        $chatcolor = '#0C7785';
    }
@endphp
@extends('layouts.main')
@section('title', __('Dashboard'))
@section('breadcrumb')
    <div class="col-md-12">
        <div class="page-header-title">
            <h4 class="m-b-10">{{ __('Dashboard') }}</h4>
        </div>
    </div>
@endsection
@section('content')
    <div class="row">
        <div class="col-12 d-flex">
            <div class="mb-4 row">
                <div class="mb-3 col-xxl-8">
                    <div class="row h-100">
                            <div class="col-lg-3 col-6 card-event">
                                <a href="users">
                                    <div class="card comp-card number-card">
                                        <div class="card-body">
                                            <div class="row align-items-center">
                                                <div class="col-12 m-b-20">
                                                    <i class="text-white ti ti-users bg-primary"></i>
                                                </div>
                                                <div class="col-12">
                                                    <h6 class="m-b-20 text-muted">{{ __('Total User') }}</h6>
                                                    <h3 class="text-primary">{{ isset($user) ? $user : 0 }}</h3>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </a>
                            </div>


                            <div class="col-lg-3 col-6 card-event">
                                <a href="contactos">
                                    <div class="card comp-card number-card">
                                        <div class="card-body">
                                            <div class="row align-items-center">
                                                <div class="col-12 m-b-20">
                                                    <i class="text-white ti ti-ad-2 bg-info"></i>
                                                </div>
                                                <div class="col-12">
                                                    <h6 class="m-b-20 text-muted">{{ __('Total Contacto') }}</h6>
                                                    <h3 class="text-info">{{ isset($contactos) ? $contactos : 0 }}</h3>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </a>
                            </div>
                    </div>
                </div>

                <div class="mb-3 col-xxl-4">
                    <div class="row">

                        <div class="col-lg-8 col-sm-6 col-12 dash-card-responsive">
                            <div class="m-0 card comp-card">
                                <div class="card-body admin-wish-card">
                                    <div class="row h-100">
                                        <div class="col-xxl-12">
                                            <div class="row">
                                                <h4 id="wishing">{{ 'Good morning ,' }}</h4>
                                            </div>
                                        </div>
                                        <h4 class="f-w-400">
                                            <a href="{{ Storage::url(Auth::user()->avatar) }}" target="_new">
                                                <img src="{{ Illuminate\Support\Facades\File::exists(Storage::path(Auth::user()->avatar)) ? Storage::url(Auth::user()->avatar) : Auth::user()->avatar_image }}"
                                                    class="me-2 img-thumbnail rounded-circle" width="50px"
                                                    height="50px"></a>
                                            <span class="text-muted">{{ Auth::user()->name }}</span>
                                        </h4>
                                        <p>
                                            {{ __('¡Hola! Puede agregar rápidamente un nuevo contacto') }}
                                        </p>
                                        <div class="dropdown quick-add-btn">
                                            @canany(['create-form', 'create-poll', 'create-event'])
                                                <a class="btn-q-add dropdown-toggle dash-btn btn btn-default btn-light-primary"
                                                    data-bs-toggle="dropdown" href="#" role="button"
                                                    aria-haspopup="false" aria-expanded="false">
                                                    <i class="ti ti-plus drp-icon"></i>
                                                    <span class="ms-1">{{ __('Quick add') }}</span>
                                                </a>
                                            @endcanany
                                            <div class="dropdown-menu">
                                                @if (\Auth::user()->can('create-event'))
                                                    <a href="contactos/create" data-size="md"
                                                        data-title="Nuevo Contacto" class="dropdown-item"
                                                        data-bs-placement="top "><span>{{ __('Nuevo Contacto') }}</span></a>
                                                @endif
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

@endsection
@push('style')
    <link rel="stylesheet" type="text/css" href="{{ asset('vendor/dragdrop/dragula.min.css') }}">
    <link rel="stylesheet" href="{{ asset('assets/css/plugins/datepicker-bs5.min.css') }}">
@endpush
@push('script')
    <script src="{{ asset('vendor/apex-chart/apexcharts.min.js') }}"></script>
    <script src="{{ asset('vendor/dragdrop/dragula.min.js') }}"></script>
    <script src="{{ asset('assets/js/plugins/choices.min.js') }}"></script>
    <script src="{{ asset('vendor/moment.min.js') }}"></script>
    <script src="{{ asset('assets/js/plugins/datepicker-full.min.js') }}"></script>


    <script>
        var today = new Date()
        var curHr = today.getHours()
        var target = document.getElementById("wishing");

        if (curHr < 12) {
            target.innerHTML = "Good Morning,";
        } else if (curHr < 17) {
            target.innerHTML = "Good Afternoon,";
        } else {
            target.innerHTML = "Good Evening,";
        }
    </script>
@endpush
