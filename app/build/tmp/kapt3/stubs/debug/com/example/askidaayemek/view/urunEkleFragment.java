package com.example.askidaayemek.view;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000R\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0014\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0015\u001a\u00020\u0016H\u0002J\b\u0010\u0017\u001a\u00020\u0016H\u0002J\b\u0010\u0018\u001a\u00020\u0016H\u0002J\b\u0010\u0019\u001a\u00020\u0016H\u0002J\b\u0010\u001a\u001a\u00020\u0016H\u0016J\u001a\u0010\u001b\u001a\u00020\u00162\u0006\u0010\u001c\u001a\u00020\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u001fH\u0016J\b\u0010 \u001a\u00020\u0016H\u0002J\b\u0010!\u001a\u00020\u0016H\u0002J\u0012\u0010\"\u001a\u00020\u00162\b\u0010#\u001a\u0004\u0018\u00010\u0012H\u0002J\b\u0010$\u001a\u00020\u0016H\u0002J\u0010\u0010%\u001a\u00020\u00162\u0006\u0010&\u001a\u00020\u0010H\u0002J\b\u0010\'\u001a\u00020\u0016H\u0002J\u0012\u0010(\u001a\u0004\u0018\u00010\u00122\u0006\u0010)\u001a\u00020\u0014H\u0002J@\u0010*\u001a\u00020\u00162\u0006\u0010+\u001a\u00020\u00122\u0006\u0010,\u001a\u00020\u00122\u0006\u0010-\u001a\u00020\u00122\u0006\u0010.\u001a\u00020\u00122\u0006\u0010/\u001a\u00020\u00122\u0006\u00100\u001a\u00020\u00122\u0006\u00101\u001a\u00020\u0012H\u0002J\b\u00102\u001a\u00020\u0016H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0010X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0013\u001a\u0004\u0018\u00010\u0014X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u00063"}, d2 = {"Lcom/example/askidaayemek/view/urunEkleFragment;", "Landroidx/fragment/app/Fragment;", "()V", "_binding", "Lcom/example/askidaayemek/databinding/FragmentUrunEkleBinding;", "activityResultLauncher", "Landroidx/activity/result/ActivityResultLauncher;", "Landroid/content/Intent;", "auth", "Lcom/google/firebase/auth/FirebaseAuth;", "binding", "getBinding", "()Lcom/example/askidaayemek/databinding/FragmentUrunEkleBinding;", "db", "Lcom/google/firebase/firestore/FirebaseFirestore;", "guncellenecekUrun", "Lcom/example/askidaayemek/dataClass/urun;", "permissionLauncher", "", "secilenGorselUri", "Landroid/net/Uri;", "gorselSec", "", "guncellemeIslemi", "haritadanGelenKonumuDinle", "hideKeyboard", "onDestroyView", "onViewCreated", "view", "Landroid/view/View;", "savedInstanceState", "Landroid/os/Bundle;", "popupGoster", "registerLaunchers", "resimYukle", "data", "setupAddMode", "setupEditMode", "urun", "setupListeners", "uriToBase64", "uri", "veritabaninaKaydet", "kategori", "ad", "not", "konum", "miktar", "uid", "gorsel", "yayinlaIslemi", "app_debug"})
public final class urunEkleFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private com.example.askidaayemek.databinding.FragmentUrunEkleBinding _binding;
    private androidx.activity.result.ActivityResultLauncher<android.content.Intent> activityResultLauncher;
    private androidx.activity.result.ActivityResultLauncher<java.lang.String> permissionLauncher;
    @org.jetbrains.annotations.Nullable()
    private android.net.Uri secilenGorselUri;
    @org.jetbrains.annotations.Nullable()
    private com.example.askidaayemek.dataClass.urun guncellenecekUrun;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.firestore.FirebaseFirestore db = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.auth.FirebaseAuth auth = null;
    
    public urunEkleFragment() {
        super();
    }
    
    private final com.example.askidaayemek.databinding.FragmentUrunEkleBinding getBinding() {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupListeners() {
    }
    
    private final void haritadanGelenKonumuDinle() {
    }
    
    private final void setupEditMode(com.example.askidaayemek.dataClass.urun urun) {
    }
    
    private final void setupAddMode() {
    }
    
    private final void popupGoster() {
    }
    
    private final void hideKeyboard() {
    }
    
    private final void registerLaunchers() {
    }
    
    private final void guncellemeIslemi() {
    }
    
    private final void yayinlaIslemi() {
    }
    
    private final void veritabaninaKaydet(java.lang.String kategori, java.lang.String ad, java.lang.String not, java.lang.String konum, java.lang.String miktar, java.lang.String uid, java.lang.String gorsel) {
    }
    
    private final void resimYukle(java.lang.String data) {
    }
    
    private final java.lang.String uriToBase64(android.net.Uri uri) {
        return null;
    }
    
    private final void gorselSec() {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
}