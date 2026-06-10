package com.example.askidaayemek.view;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u000e\u001a\u00020\u000fH\u0002J\u0014\u0010\u0010\u001a\u00020\u000f2\n\u0010\u0011\u001a\u00060\u0012j\u0002`\u0013H\u0002J\u0018\u0010\u0014\u001a\u00020\u000f2\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0016H\u0002J\b\u0010\u0018\u001a\u00020\u0019H\u0002J\u0010\u0010\u001a\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u0016H\u0002J\"\u0010\u001c\u001a\u00020\u000f2\u0006\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001f\u001a\u00020\u001e2\b\u0010 \u001a\u0004\u0018\u00010!H\u0016J\b\u0010\"\u001a\u00020\u000fH\u0016J\u001a\u0010#\u001a\u00020\u000f2\u0006\u0010$\u001a\u00020%2\b\u0010&\u001a\u0004\u0018\u00010\'H\u0016J\u0010\u0010(\u001a\u00020\u000f2\u0006\u0010\u001b\u001a\u00020\u0016H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\tR\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006)"}, d2 = {"Lcom/example/askidaayemek/view/girisLoginFragment;", "Landroidx/fragment/app/Fragment;", "()V", "_binding", "Lcom/example/askidaayemek/databinding/FragmentGirisLoginBinding;", "auth", "Lcom/google/firebase/auth/FirebaseAuth;", "binding", "getBinding", "()Lcom/example/askidaayemek/databinding/FragmentGirisLoginBinding;", "db", "Lcom/google/firebase/firestore/FirebaseFirestore;", "googleSignInClient", "Lcom/google/android/gms/auth/api/signin/GoogleSignInClient;", "girisTamamlandi", "", "handleLoginError", "e", "Ljava/lang/Exception;", "Lkotlin/Exception;", "hataGoster", "baslik", "", "mesaj", "isInternetAvailable", "", "kullaniciTipiniKontrolEtVeYonlendir", "uid", "onActivityResult", "requestCode", "", "resultCode", "data", "Landroid/content/Intent;", "onDestroyView", "onViewCreated", "view", "Landroid/view/View;", "savedInstanceState", "Landroid/os/Bundle;", "varsayilanKullaniciOlustur", "app_debug"})
public final class girisLoginFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private com.example.askidaayemek.databinding.FragmentGirisLoginBinding _binding;
    private com.google.firebase.auth.FirebaseAuth auth;
    private com.google.firebase.firestore.FirebaseFirestore db;
    private com.google.android.gms.auth.api.signin.GoogleSignInClient googleSignInClient;
    
    public girisLoginFragment() {
        super();
    }
    
    private final com.example.askidaayemek.databinding.FragmentGirisLoginBinding getBinding() {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void handleLoginError(java.lang.Exception e) {
    }
    
    private final void girisTamamlandi() {
    }
    
    private final void hataGoster(java.lang.String baslik, java.lang.String mesaj) {
    }
    
    private final void kullaniciTipiniKontrolEtVeYonlendir(java.lang.String uid) {
    }
    
    private final void varsayilanKullaniciOlustur(java.lang.String uid) {
    }
    
    private final boolean isInternetAvailable() {
        return false;
    }
    
    @java.lang.Override()
    public void onActivityResult(int requestCode, int resultCode, @org.jetbrains.annotations.Nullable()
    android.content.Intent data) {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
}