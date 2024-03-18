<?php

use App\Http\Controllers\Auth\LoginController;
use App\Http\Controllers\ContactoController;
use App\Http\Controllers\HomeController;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\SettingsController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use App\Http\Controllers\LanguageController;
use App\Http\Controllers\PermissionController;
use App\Http\Controllers\ProfileController;
use App\Http\Controllers\RoleController;
use App\Http\Controllers\UserController;
use Illuminate\Support\Facades\Artisan;


Auth::routes(['verify' => true]);
Auth::routes();


Route::group(['middleware' => ['auth', 'xss', 'Setting', 'verified', '2fa', 'verified_phone']], function () {
    Route::get('/home', [HomeController::class, 'index'])->name('home');
    Route::resource('profile', ProfileController::class);
    Route::resource('users', UserController::class)->except(['show']);
    Route::resource('permission', PermissionController::class);
    Route::resource('roles', RoleController::class);
    Route::post('/role-permission/{id}', [RoleController::class, 'assignPermission'])->name('roles_permit');

    Route::resource('contactos', ContactoController::class)->except(['show']);

    Route::post('update-avatar/{id}', [ProfileController::class, 'updateAvatar'])->name('update-avatar');

    Route::get('change-language/{lang}', [LanguageController::class, 'changeLanquage'])->name('change.language');
    Route::get('manage-language/{lang}', [LanguageController::class, 'manageLanguage'])->name('manage.language');
    Route::post('store-language-data/{lang}', [LanguageController::class, 'storeLanguageData'])->name('store.language.data');
    Route::get('create-language', [LanguageController::class, 'createLanguage'])->name('create.language');
    Route::post('store-language', [LanguageController::class, 'storeLanguage'])->name('store.language');
    Route::delete('/lang/{lang}', [LanguageController::class, 'destroyLang'])->name('lang.destroy');

    Route::get('/leads/order', [HomeController::class, 'leadsorder'])->name('leads.order');
    Route::post('/change/theme/mode', [HomeController::class, 'changeThememode'])->name('change.theme.mode');
    Route::post('/chart', [HomeController::class, 'formchart'])->name('get.chart.data')->middleware(['auth', 'Setting', 'xss']);
    Route::post('read/notification', [HomeController::class, 'read_notification'])->name('read.notification');

    Route::post('/user/status/{id}', [UserController::class, 'userStatus'])->name('users.status');
    Route::get('/users/grid/{id?}', [UserController::class, 'grid_view'])->name('grid.view');

    Route::post('update-profile-login/{id}', [ProfileController::class, 'updateLogin'])->name('update-login');
    Route::get('account-status/{id}', [UserController::class, 'accountStatus'])->name('account.status');
    Route::get('users/verified/{id}', [UserController::class, 'useremailverified'])->name('user.verified');
    Route::get('users/phoneverified/{id}', [UserController::class, 'userphoneverified'])->name('user.phoneverified');
    Route::get('profile-status', [ProfileController::class, 'profileStatus'])->name('profile.status');
    Route::post('profile/update/{id}', [ProfileController::class, 'update'])->name('profile.update');

    Route::post('settings/sms-setting/update', [SettingsController::class, 'smsSettingUpdate'])->name('settings/sms-setting/update');

    Route::post('settings/email-setting/update', [SettingsController::class, 'emailSettingUpdate'])->name('settings/email-setting/update');
    Route::post('settings/auth-settings/update', [SettingsController::class, 'authSettingsUpdate'])->name('settings/auth-settings/update');
    Route::post('test-mail', [SettingsController::class, 'testSendMail'])->name('test.send.mail');
    Route::get('setting/{id}', [SettingsController::class, 'loadsetting'])->name('setting');
    Route::post('settings/app-name/update', [SettingsController::class, 'appNameUpdate'])->name('settings/app-name/update');
    Route::post('settings/app-logo/update', [SettingsController::class, 'appLogoUpdate'])->name('settings/app-logo/update');
    Route::post('settings/GoogleCalender/update', [SettingsController::class, 'GoogleCalender'])->name('settings/GoogleCalender/update');
    Route::post('settings/GoogleMap/update', [SettingsController::class, 'GoogleMapUpdate'])->name('settings/GoogleMap/update')->middleware(['auth', 'Setting', 'xss']);
    Route::post('settings/pusher-setting/update', [SettingsController::class, 'pusherSettingUpdate'])->name('settings/pusher-setting/update');
    Route::post('settings/wasabi-setting/update', [SettingsController::class, 'wasabiSettingUpdate'])->name('settings/wasabi-setting/update');
    Route::post('settings/captcha-setting/update', [SettingsController::class, 'captchaSettingUpdate'])->name('settings/captcha-setting/update');
    Route::post('settings/cookie-setting/update', [SettingsController::class, 'cookieSettingUpdate'])->name('settings/cookie-setting/update');
    Route::post('settings/seo-setting/update', [SettingsController::class, 'seoSettingUpdate'])->name('settings/seo-setting/update');

    Route::get('frontend-setting', [SettingsController::class, 'frontendsetting'])->name('frontend.page');
    Route::post('frontend-setting/store', [SettingsController::class, 'frontendsettingstore'])->name('frontend.page.store');
    Route::post('menu-setting/store', [SettingsController::class, 'menusettingstore'])->name('menu.page.store');
    Route::post('price-setting/store', [SettingsController::class, 'pricesettingstore'])->name('price.page.store');
    Route::post('feature-setting/store', [SettingsController::class, 'featuresettingstore'])->name('feature.page.store');
    Route::post('sidefeature-setting/store', [SettingsController::class, 'sidefeaturesettingstore'])->name('sidefeature.page.store');
    Route::post('privacy-setting/store', [SettingsController::class, 'privacysettingstore'])->name('privacy.page.store');
    Route::post('contactus-setting/store', [SettingsController::class, 'contactussettingstore'])->name('contactus.page.store');
    Route::post('termcondition-setting/store', [SettingsController::class, 'termconditionsettingstore'])->name('termcondition.page.store');
    Route::post('faq-setting/store', [SettingsController::class, 'faqsettingstore'])->name('faq.page.store');
    Route::post('testimonial-setting/store', [SettingsController::class, 'testimonialStore'])->name('testimonialfronted.store');
    Route::post('recaptcha-setting/store', [SettingsController::class, 'recaptchasettingstore'])->name('recaptcha.page.store');
    Route::post('login-setting/store', [SettingsController::class, 'loginsettingstore'])->name('login.page.store');
    Route::get('/settings', [SettingsController::class, 'index'])->name('settings');
    Route::get('/test-mail', [SettingsController::class, 'testMail'])->name('test.mail');

});



Route::get('/{lang?}', [HomeController::class, 'landingPage'])->name('landingpage');

Route::group(['middleware' => ['Setting', 'xss']], function () {
    Auth::routes(['verify' => true]);

    Route::get('/login/{lang?}', [LoginController::class, 'showLoginForm'])->name('login');

});


Route::post('settings/stripe-setting/update', [SettingsController::class, 'paymentSettingUpdate'])->name('settings/stripe-setting/update');
Route::post('settings/social-setting/update', [SettingsController::class, 'socialSettingUpdate'])->name('settings/social-setting/update');


Route::get('form-detail/id', [HomeController::class, 'form_details'])->name('form.details');

Route::impersonate();
Route::get('users/{id}/impersonate', [UserController::class, 'impersonate'])->name('users.impersonate');
Route::get('impersonate/leave', [UserController::class, 'leaveimpersonate'])->name('impersonate.leave');

Route::any('/cookie-consent', [SettingsController::class, 'CookieConsent'])->name('cookie-consent')->middleware(['xss']);

Route::any('/config-cache', function () {
    Artisan::call('cache:clear');
    Artisan::call('route:clear');
    Artisan::call('view:clear');
    Artisan::call('optimize:clear');
    return redirect()->back()->with('success', __('Cache clear successfully.'));
})->name('config.cache')->middleware(['xss']);

Route::get('/invisible', function () {
    return view('invisible');
});
Route::post('/invisible', function (Request $request) {
    $request->validate([
        'g-recaptcha-response' => 'required|captcha'
    ]);
    return 'Data is valid';
});

Route::post('/2fa', function () {
    return redirect(URL()->previous());
})->name('2fa')->middleware('2fa');
