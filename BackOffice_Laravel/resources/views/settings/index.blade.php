@php
    use App\Facades\UtilityFacades;
    $lang = \App\Facades\UtilityFacades::getValByName('default_language');
    $primary_color = \App\Facades\UtilityFacades::getsettings('color');

    if (isset($primary_color)) {
        $color = $primary_color;
    } else {
        $color = 'theme-4';
    }
    $roles = App\Models\Role::whereNotIn('name', ['Super Admin', 'Admin'])
        ->pluck('name', 'name')
        ->all();
@endphp

@extends('layouts.main')
@section('title', __('Settings'))
@section('breadcrumb')
    <div class="col-md-12">
        <div class="page-header-title">
            <h4 class="m-b-10">{{ __('Settings') }}</h4>
        </div>
        <ul class="breadcrumb">
            <li class="breadcrumb-item">{!! Html::link(route('home'), __('Dashboard'), []) !!}</li>
            <li class="breadcrumb-item active"> {{ __('Settings') }}</li>
        </ul>
    </div>
@endsection
@section('content')
    <div class="row">
        <!-- [ sample-page ] start -->
        <div class="col-sm-12">
            <div class="row">
                <div class="col-xl-3">
                    <div class="mt-3 card sticky-top">
                        <div class="list-group list-group-flush" id="useradd-sidenav">
                            <a href="#app-setting"
                                class="border-0 list-group-item list-group-item-action">{{ __('Ajustes Multimedia') }}
                                <div class="float-end"><i class="ti ti-chevron-right"></i></div>
                            </a>
                            <a href="#general-setting"
                                class="border-0 list-group-item list-group-item-action">{{ __('Configuración general') }}
                                <div class="float-end"><i class="ti ti-chevron-right"></i></div>
                            </a>
                        </div>
                    </div>
                </div>
                <div class="col-xl-9">

                    <div id="app-setting" class="pt-0 card">
                        {!! Form::open([
                            'route' => ['settings/app-name/update'],
                            'enctype' => 'multipart/form-data',
                        ]) !!}
                        <div class="card-header">
                            <h5> {{ __('Ajustes Multimedia') }}</h5>
                        </div>
                        <div class="card-body">
                            <div class="pt-0 row">
                                <div class="col-lg-4 col-sm-6 col-md-6">
                                    <div class="card">
                                        <div class="card-header">
                                            <h5>{{ __('App Dark Logo') }}</h5>
                                        </div>
                                        <div class="pt-0 card-body">
                                            <div class="inner-content">
                                                <div class="py-2 mt-4 text-center logo-content dark-logo-content">
                                                    <a href="{{ Utility::getpath('app_dark_logo') ? Storage::url('appLogo/app-dark-logo.png') : '' }}"
                                                        target="_blank">
                                                        <img src="{{ Utility::getpath('app_dark_logo') ? Storage::url('appLogo/app-dark-logo.png') : '' }}"
                                                            id="app_dark">
                                                    </a>
                                                </div>
                                                <div class="mt-3 text-center choose-files">
                                                    <label for="app_dark_logo">
                                                        <div class="bg-primary company_logo_update"> <i
                                                                class="px-1 ti ti-upload"></i>{{ __('Choose file here') }}
                                                        </div>
                                                        {{ Form::file('app_dark_logo', ['class' => 'form-control file', 'id' => 'app_dark_logo', 'onchange' => "document.getElementById('app_dark').src = window.URL.createObjectURL(this.files[0])", 'data-filename' => 'app_dark_logo']) }}
                                                    </label>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-4 col-sm-6 col-md-6">
                                    <div class="card">
                                        <div class="card-header">
                                            <h5>{{ __('App Light Logo') }}</h5>
                                        </div>
                                        <div class="pt-0 card-body bg-primary">
                                            <div class="inner-content">
                                                <div class="py-2 mt-4 text-center logo-content light-logo-content">
                                                    <a href="{{ Utility::getpath('app_logo') ? Storage::url('appLogo/app-logo.png') : Storage::url('appLogo/78x78.png') }}"
                                                        target="_blank">
                                                        <img src="{{ Utility::getpath('app_logo') ? Storage::url('appLogo/app-logo.png') : Storage::url('appLogo/78x78.png') }}"
                                                            id="app_light">
                                                    </a>
                                                </div>
                                                <div class="mt-3 text-center choose-files">
                                                    <label for="app_logo">
                                                        <div class="company_logo_update w-logo"> <i
                                                                class="px-1 ti ti-upload"></i>{{ __('Choose file here') }}
                                                        </div>
                                                        {{ Form::file('app_logo', ['class' => 'form-control file', 'id' => 'app_logo', 'onchange' => "document.getElementById('app_light').src = window.URL.createObjectURL(this.files[0])", 'data-filename' => 'app_logo']) }}
                                                    </label>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="col-lg-4 col-sm-6 col-md-6">
                                    <div class="card">
                                        <div class="card-header">
                                            <h5>{{ __('App Favicon Logo') }}</h5>
                                        </div>
                                        <div class="pt-0 card-body">
                                            <div class="inner-content">
                                                <div class="py-2 mt-4 text-center logo-content">
                                                    <a href="{{ Utility::getpath('favicon_logo') ? Storage::url('appLogo/app-favicon-logo.png') : '' }}"
                                                        target="_blank">
                                                        <img height="35px"
                                                            src="{{ Utility::getpath('favicon_logo') ? Storage::url('appLogo/app-favicon-logo.png') : '' }}"
                                                            id="app_favicon">
                                                    </a>
                                                </div>
                                                <div class="mt-3 text-center choose-files">
                                                    <label for="favicon_logo">
                                                        <div class="bg-primary company_logo_update"> <i
                                                                class="px-1 ti ti-upload"></i>{{ __('Choose file here') }}
                                                        </div>
                                                        {{ Form::file('favicon_logo', ['class' => 'form-control file', 'id' => 'favicon_logo', 'onchange' => "document.getElementById('app_favicon').src = window.URL.createObjectURL(this.files[0])", 'data-filename' => 'favicon_logo']) }}
                                                    </label>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="form-group">
                                    {{ Form::label('app_name', __('Application Name'), ['class' => 'form-label']) }}
                                    {!! Form::text('app_name', Utility::getsettings('app_name'), [
                                        'class' => 'form-control',
                                        'placeholder' => __('Enter application name'),
                                    ]) !!}
                                </div>
                            </div>
                        </div>
                        <div class="card-footer">
                            <div class="text-end">
                                {!! Form::button(__('Save'), ['type' => 'submit', 'class' => 'btn btn-primary']) !!}
                            </div>
                        </div>
                        {!! Form::close() !!}
                    </div>

                    <div id="general-setting" class="">
                        {!! Form::open([
                            'route' => ['settings/auth-settings/update'],
                            'method' => 'POST',
                            'novalidate',
                            'data-validate',
                            'enctype' => 'multipart/form-data',
                        ]) !!}
                        <div class="card" id="settings-card">
                            <div class="card-header">
                                <h5>{{ __('General Settings') }}</h5>
                            </div>
                            <div class="card-body">
                                <div class="row">




                                    <div class="mt-2 col-sm-12">
                                        <div class="form-group d-flex align-items-center row">
                                            <h4 class="small-title">{{ __('Theme Customizer') }}</h4>
                                            <div class="setting-card setting-logo-box">
                                                <div class="row">
                                                    <div class="col-lg-4 col-xl-4 col-md-4">
                                                        <h6 class="mt-2">
                                                            <i data-feather="credit-card"
                                                                class="me-2"></i>{{ __('Primary color settings') }}
                                                        </h6>
                                                        <hr class="my-2" />
                                                        <div class="theme-color themes-color">
                                                            <a href="#!"
                                                                class="{{ $color == 'theme-1' ? 'active_color' : '' }}"
                                                                data-value="theme-1" onclick="check_theme('theme-1')"></a>
                                                            <input type="radio" class="theme_color d-none"
                                                                name="color" value="theme-1">
                                                            <a href="#!"
                                                                class="{{ $color == 'theme-2' ? 'active_color' : '' }}"
                                                                data-value="theme-2" onclick="check_theme('theme-2')"></a>
                                                            <input type="radio" class="theme_color d-none"
                                                                name="color" value="theme-2">
                                                            <a href="#!"
                                                                class="{{ $color == 'theme-3' ? 'active_color' : '' }}"
                                                                data-value="theme-3" onclick="check_theme('theme-3')"></a>
                                                            <input type="radio" class="theme_color d-none"
                                                                name="color" value="theme-3">
                                                            <a href="#!"
                                                                class="{{ $color == 'theme-4' ? 'active_color' : '' }}"
                                                                data-value="theme-4" onclick="check_theme('theme-4')"></a>
                                                            <input type="radio" class="theme_color d-none"
                                                                name="color" value="theme-4">
                                                            <a href="#!"
                                                                class="{{ $color == 'theme-5' ? 'active_color' : '' }}"
                                                                data-value="theme-5" onclick="check_theme('theme-5')"></a>
                                                            <input type="radio" class="theme_color d-none"
                                                                name="color" value="theme-5">
                                                            <br>
                                                            <a href="#!"
                                                                class="{{ $color == 'theme-6' ? 'active_color' : '' }}"
                                                                data-value="theme-6" onclick="check_theme('theme-6')"></a>
                                                            <input type="radio" class="theme_color d-none"
                                                                name="color" value="theme-6">
                                                            <a href="#!"
                                                                class="{{ $color == 'theme-7' ? 'active_color' : '' }}"
                                                                data-value="theme-7" onclick="check_theme('theme-7')"></a>
                                                            <input type="radio" class="theme_color d-none"
                                                                name="color" value="theme-7">
                                                            <a href="#!"
                                                                class="{{ $color == 'theme-8' ? 'active_color' : '' }}"
                                                                data-value="theme-8" onclick="check_theme('theme-8')"></a>
                                                            <input type="radio" class="theme_color d-none"
                                                                name="color" value="theme-8">
                                                            <a href="#!"
                                                                class="{{ $color == 'theme-9' ? 'active_color' : '' }}"
                                                                data-value="theme-9" onclick="check_theme('theme-9')"></a>
                                                            <input type="radio" class="theme_color d-none"
                                                                name="color" value="theme-9">
                                                            <a href="#!"
                                                                class="{{ $color == 'theme-10' ? 'active_color' : '' }}"
                                                                data-value="theme-10"
                                                                onclick="check_theme('theme-10')"></a>
                                                            <input type="radio" class="theme_color d-none"
                                                                name="color" value="theme-10">
                                                        </div>
                                                    </div>
                                                    <div class="col-lg-4 col-xl-4 col-md-4">
                                                        <h6 class="mt-2">
                                                            <i data-feather="layout"
                                                                class="me-2"></i>{{ __('Sidebar settings') }}
                                                        </h6>
                                                        <hr class="my-2" />
                                                        <div class="form-check form-switch">
                                                            {!! Form::checkbox(
                                                                'transparent_layout',
                                                                null,
                                                                Utility::getsettings('transparent_layout') == 'on' ? 'checked' : '',
                                                                [
                                                                    'data-onstyle' => 'primary',
                                                                    'id' => 'cust-theme-bg',
                                                                    'class' => 'form-check-input',
                                                                ],
                                                            ) !!}
                                                            {!! Form::label('cust-theme-bg', __('Transparent layout'), ['class' => 'form-check-label f-w-600 pl-1']) !!}
                                                        </div>
                                                    </div>
                                                    <div class="col-lg-4 col-xl-4 col-md-4">
                                                        <h6 class="mt-2">
                                                            <i data-feather="sun"
                                                                class="me-2"></i>{{ __('Layout settings') }}
                                                        </h6>
                                                        <hr class="my-2" />
                                                        <div class="mt-2 form-check form-switch">
                                                            {!! Form::checkbox('dark_mode', null, Utility::getsettings('dark_mode') == 'on' ? true : false, [
                                                                'id' => 'cust-darklayout',
                                                                'class' => 'form-check-input',
                                                            ]) !!}
                                                            {!! Form::label('cust-darklayout', __('Dark Layout'), ['class' => 'form-check-label f-w-600 pl-1']) !!}
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                    <hr>

                                </div>
                            </div>
                            <div class="card-footer">
                                <div class="text-end">
                                    {!! Form::button(__('Save'), ['type' => 'submit', 'class' => 'btn btn-primary']) !!}
                                </div>
                            </div>
                        </div>
                        {!! Form::close() !!}
                    </div>

                </div>
                <!-- [ sample-page ] end -->
            </div>
            <!-- [ Main Content ] end -->
        </div>
    </div>
@endsection
@push('script')
    <script src="{{ asset('assets/js/plugins/bootstrap-switch-button.min.js') }}"></script>
    <script src="{{ asset('assets/js/plugins/choices.min.js') }}"></script>
    <script>
        var textRemove = new Choices(
            document.getElementById('choices-text-remove-button'), {
                delimiter: ',',
                editItems: true,
                removeItemButton: true,
            }
        );
        feather.replace();
        var pctoggle = document.querySelector("#pct-toggler");
        if (pctoggle) {
            pctoggle.addEventListener("click", function() {
                if (
                    !document.querySelector(".pct-customizer").classList.contains("active")
                ) {
                    document.querySelector(".pct-customizer").classList.add("active");
                } else {
                    document.querySelector(".pct-customizer").classList.remove("active");
                }
            });
        }
        var custthemebg = document.querySelector("#cust-theme-bg");
        custthemebg.addEventListener("click", function() {
            if (custthemebg.checked) {
                document.querySelector(".dash-sidebar").classList.add("transprent-bg");
                document
                    .querySelector(".dash-header:not(.dash-mob-header)")
                    .classList.add("transprent-bg");
            } else {
                document.querySelector(".dash-sidebar").classList.remove("transprent-bg");
                document
                    .querySelector(".dash-header:not(.dash-mob-header)")
                    .classList.remove("transprent-bg");
            }
        });

        var themescolors = document.querySelectorAll(".themes-color > a");
        for (var h = 0; h < themescolors.length; h++) {
            var c = themescolors[h];
            c.addEventListener("click", function(event) {
                var targetElement = event.target;
                if (targetElement.tagName == "SPAN") {
                    targetElement = targetElement.parentNode;
                }
                var temp = targetElement.getAttribute("data-value");
                removeClassByPrefix(document.querySelector("body"), "theme-");
                document.querySelector("body").classList.add(temp);
            });
        }
        var custdarklayout = document.querySelector("#cust-darklayout");
        custdarklayout.addEventListener("click", function() {
            if (custdarklayout.checked) {
                document.querySelector(".m-header > .b-brand > img").setAttribute("src",
                    "{{ Storage::url(Utility::getsettings('app_logo')) }}");
                document.querySelector("#main-style-link").setAttribute("href",
                    "{{ asset('assets/css/style-dark.css') }}");
            } else {
                document.querySelector(".m-header > .b-brand > img").setAttribute("src",
                    "{{ Storage::url(Utility::getsettings('app_dark_logo')) }}");
                document.querySelector("#main-style-link").setAttribute("href",
                    "{{ asset('assets/css/style.css') }}");
            }
        });

        function check_theme(color_val) {
            $('.theme-color').prop('checked', false);
            $('input[value="' + color_val + '"]').prop('checked', true);
        }

        function enablecookie() {
            const element = $('#enable_cookie').is(':checked');
            $('.cookieDiv').addClass('d-none');
            if (element == true) {
                $('.cookieDiv').removeClass('d-none');
                $("#cookie_logging").attr('checked', true);
            } else {
                $('.cookieDiv').addClass('d-none');
                $("#cookie_logging").attr('checked', false);
            }
        }

        function enableseo() {
            const element = $('#seo_setting').is(':checked');
            $('.seoDiv').addClass('d-none');
            if (element == true) {
                $('.seoDiv').removeClass('d-none');
            } else {
                $('.seoDiv').addClass('d-none');
            }
        }

        $('body').on('click', '.send_mail', function() {
            var action = $(this).data('action');
            var modal = $('#common_modal');
            $.get(action, function(response) {
                modal.find('.modal-title').html('{{ __('Test Mail') }}');
                modal.find('.body').html(response);
                modal.modal('show');
            })
        });
        $(document).ready(function() {
            $(".socialsetting").trigger("select");
        });
        $(document).on('change', ".socialsetting", function() {
            var test = $(this).val();
            if ($(this).is(':checked')) {
                if (test == 'google') {
                    $("#google").fadeIn(500);
                    $("#google").removeClass('d-none');
                } else if (test == 'facebook') {
                    $("#facebook").fadeIn(500);
                    $("#facebook").removeClass('d-none');
                } else if (test == 'github') {
                    $("#github").fadeIn(500);
                    $("#github").removeClass('d-none');
                } else if (test == 'linkedin') {
                    $("#linkedin").fadeIn(500);
                    $("#linkedin").removeClass('d-none');
                }
            } else {
                if (test == 'google') {
                    $("#google").fadeOut(500);
                    $("#google").addClass('d-none');
                } else if (test == 'facebook') {
                    $("#facebook").fadeOut(500);
                    $("#facebook").addClass('d-none');
                } else if (test == 'github') {
                    $("#github").fadeOut(500);
                    $("#github").addClass('d-none');
                } else if (test == 'linkedin') {
                    $("#linkedin").fadeOut(500);
                    $("#linkedin").addClass('d-none');
                }
            }
        });
        $(document).ready(function() {
            if ($("input[name$='captcha']").is(':checked')) {
                $("#recaptcha").fadeIn(500);
                $("#recaptcha").removeClass('d-none');
            } else {
                $("#recaptcha").fadeOut(500);
                $("#recaptcha").addClass('d-none');
            }
            $(".paymenttsetting").trigger("select");
        });
        $(document).on('change', ".paymenttsetting", function() {
            var test = $(this).val();
            if ($(this).is(':checked')) {
                if (test == 'razorpay') {
                    $("#razorpay").fadeIn(500);
                    $("#razorpay").removeClass('d-none');
                } else if (test == 'stripe') {
                    $("#stripe").fadeIn(500);
                    $("#stripe").removeClass('d-none');
                } else if (test == 'paytm') {
                    $("#paytm").fadeIn(500);
                    $("#paytm").removeClass('d-none');
                } else if (test == 'paypal') {
                    $("#paypal").fadeIn(500);
                    $("#paypal").removeClass('d-none');
                } else if (test == 'flutterwave') {
                    $("#flutterwave").fadeIn(500);
                    $("#flutterwave").removeClass('d-none');
                } else if (test == 'paystack') {
                    $("#paystack").fadeIn(500);
                    $("#paystack").removeClass('d-none');
                } else if (test == 'mercado') {
                    $("#mercado").fadeIn(500);
                    $("#mercado").removeClass('d-none');
                } else if (test == 'offline') {
                    $("#offline").fadeIn(500);
                    $("#offline").removeClass('d-none');
                }
            } else {
                if (test == 'razorpay') {
                    $("#razorpay").fadeOut(500);
                    $("#razorpay").addClass('d-none');
                } else if (test == 'paytm') {
                    $("#paytm").fadeOut(500);
                    $("#paytm").removeClass('d-none');
                } else if (test == 'stripe') {
                    $("#stripe").fadeOut(500);
                    $("#stripe").addClass('d-none');
                } else if (test == 'flutterwave') {
                    $("#flutterwave").fadeIn(500);
                    $("#flutterwave").removeClass('d-none');
                } else if (test == 'paypal') {
                    $("#paypal").fadeOut(500);
                    $("#paypal").addClass('d-none');
                } else if (test == 'paystack') {
                    $("#paystack").fadeOut(500);
                    $("#paystack").addClass('d-none');
                } else if (test == 'mercado') {
                    $("#mercado").fadeIn(500);
                    $("#mercado").removeClass('d-none');
                } else if (test == 'offline') {
                    $("#offline").fadeOut(500);
                    $("#offline").addClass('d-none');
                }
            }
        });
        $(document).on('click', "input[name$='captcha']", function() {
            var test = $(this).val();
            if (test == 'hcaptcha') {
                $("#hcaptcha").fadeIn(500);
                $("#hcaptcha").removeClass('d-none');
                $("#recaptcha").addClass('d-none');
            } else {
                $("#recaptcha").fadeIn(500);
                $("#recaptcha").removeClass('d-none');
                $("#hcaptcha").addClass('d-none');
            }
        });
        $(document).on('click', "input[name$='storage_type']", function() {
            var test = $(this).val();
            if (test == 's3') {
                $("#s3").fadeIn(500);
                $("#s3").removeClass('d-none');
            } else {
                $("#s3").fadeOut(500);
            }
        });
        $(document).on('click', "input[name$='storage_type']", function() {
            var test = $(this).val();
            if (test == 'wasabi') {
                $("#wasabi").fadeIn(500);
                $("#wasabi").removeClass('d-none');
            } else {
                $("#wasabi").fadeOut(500);
            }
        });
        $(document).on('change', "#multi_sms", function() {
            if ($(this).is(':checked')) {
                $(".multi_sms").fadeIn(500);
                $('.multi_sms').removeClass('d-none');
                $('#twilio').removeClass('d-none');
            } else {
                $(".multi_sms").fadeOut(500);
                $(".multi_sms").addClass('d-none');
            }
        });

        // $(document).on('change', "#google_calender", function() {
        //     if ($(this).is(':checked')) {
        //         $(".google_calender").fadeIn(500);
        //         $('.google_calender').removeClass('d-none');
        //     } else {
        //         $(".google_calender").fadeOut(500);
        //         $(".google_calender").addClass('d-none');
        //     }
        // });

        // $(document).on('change', "#emailSettingEnableBtn", function() {
        //     if ($(this).is(':checked')) {
        //         $(".emailSettingEnableBtn").fadeIn(500);
        //         $('.emailSettingEnableBtn').removeClass('d-none');
        //     } else {
        //         $(".emailSettingEnableBtn").fadeOut(500);
        //         $(".emailSettingEnableBtn").addClass('d-none');
        //     }
        // });

        $(document).on('click', "input[name$='smssetting']", function() {
            var test = $(this).val();
            $("#twilio").fadeOut(500);
            if (test == 'twilio') {
                $("#twilio").fadeIn(500);
                $("#twilio").removeClass('d-none');
                $("#nexmo").fadeOut(500);
            } else {
                $("#nexmo").fadeIn(500);
                $("#nexmo").removeClass('d-none');
                $("#twilio").fadeOut(500);
            }
        });

        $(document).on('change', "#captchaEnableButton", function() {
            if (this.checked) {
                $('.captchaSetting').fadeIn(500);
                $(".captchaSetting").removeClass('d-none');
            } else {
                $('.captchaSetting').fadeOut(500);
                $(".captchaSetting").addClass('d-none');
            }

        })
        document.addEventListener('DOMContentLoaded', function() {
            var genericExamples = document.querySelectorAll('[data-trigger]');
            for (i = 0; i < genericExamples.length; ++i) {
                var element = genericExamples[i];
                new Choices(element, {
                    placeholderValue: 'This is a placeholder set in the config',
                    searchPlaceholderValue: 'This is a search placeholder',
                });
            }
        });
        var scrollSpy = new bootstrap.ScrollSpy(document.body, {
            target: '#useradd-sidenav',
            offset: 300
        });
        $(document).on("change", ".chnageEmailNotifyStatus", function(e) {
            var csrf = $("meta[name=csrf-token]").attr("content");
            var email = $(this).parent().find("input[name=email_notification]").is(":checked");
            var action = $(this).attr("data-url");
            $.ajax({
                type: "POST",
                url: action,
                data: {
                    _token: csrf,
                    type: 'email',
                    email_notification: email,
                },
                success: function(response) {
                    if (response.warning) {
                        show_toastr("Warning!", response.warning, "warning");
                    }
                    if (response.is_success) {
                        show_toastr("Success!", response.message, "success");
                    }
                },
            });
        });

        $(document).on("change", ".chnagesmsNotifyStatus", function(e) {
            var csrf = $("meta[name=csrf-token]").attr("content");
            var sms = $(this).parent().find("input[name=sms_notification]").is(":checked");
            var action = $(this).attr("data-url");
            $.ajax({
                type: "POST",
                url: action,
                data: {
                    _token: csrf,
                    type: 'sms',
                    sms_notification: sms,
                },
                success: function(response) {
                    if (response.warning) {
                        show_toastr("Warning!", response.warning, "warning");
                    }
                    if (response.is_success) {
                        show_toastr("Success!", response.message, "success");
                    }
                },
            });
        });

        $(document).on("change", ".chnageNotifyStatus", function(e) {
            var csrf = $("meta[name=csrf-token]").attr("content");
            var notify = $(this).parent().find("input[name=notify]").is(":checked");
            var action = $(this).attr("data-url");
            $.ajax({
                type: "POST",
                url: action,
                data: {
                    _token: csrf,
                    type: 'notify',
                    notify: notify,
                },
                success: function(response) {
                    if (response.warning) {
                        show_toastr("Warning!", response.warning, "warning");
                    }
                    if (response.is_success) {
                        show_toastr("Success!", response.message, "success");
                    }
                },
            });
        });
    </script>
@endpush
