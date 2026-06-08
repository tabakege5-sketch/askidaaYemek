package com.example.askidaayemek.view;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\t\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u0012H\u0002J\b\u0010\u0017\u001a\u00020\u0015H\u0002J\u0010\u0010\u0018\u001a\u00020\u00152\u0006\u0010\u0019\u001a\u00020\u0012H\u0002J\b\u0010\u001a\u001a\u00020\u0015H\u0002J\b\u0010\u001b\u001a\u00020\u0015H\u0002J\b\u0010\u001c\u001a\u00020\u0015H\u0016J\u001a\u0010\u001d\u001a\u00020\u00152\u0006\u0010\u001e\u001a\u00020\u001f2\b\u0010 \u001a\u0004\u0018\u00010!H\u0016J\b\u0010\"\u001a\u00020\u0015H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\rX\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2 = {"Lcom/example/askidaayemek/view/urunAnaSayfa;", "Landroidx/fragment/app/Fragment;", "()V", "_binding", "Lcom/example/askidaayemek/databinding/FragmentUrunAnaSayfaBinding;", "adapter", "Lcom/example/askidaayemek/adapter/urunAnaSayfaAdapter;", "auth", "Lcom/google/firebase/auth/FirebaseAuth;", "binding", "getBinding", "()Lcom/example/askidaayemek/databinding/FragmentUrunAnaSayfaBinding;", "db", "Lcom/google/firebase/firestore/FirebaseFirestore;", "filtreliListe", "Ljava/util/ArrayList;", "Lcom/example/askidaayemek/dataClass/urun;", "kullaniciRolu", "", "tamListe", "aramaYap", "", "text", "arayuzuRoleGoreDuzenle", "kategoriFiltrele", "kategori", "kullaniciBilgisiniGetir", "listeyiGuncelleVeMesajKontrolEt", "onDestroyView", "onViewCreated", "view", "Landroid/view/View;", "savedInstanceState", "Landroid/os/Bundle;", "verileriGetir", "app_debug"})
public final class urunAnaSayfa extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private com.example.askidaayemek.databinding.FragmentUrunAnaSayfaBinding _binding;
    private com.google.firebase.firestore.FirebaseFirestore db;
    private com.google.firebase.auth.FirebaseAuth auth;
    @org.jetbrains.annotations.NotNull()
    private java.util.ArrayList<com.example.askidaayemek.dataClass.urun> tamListe;
    @org.jetbrains.annotations.NotNull()
    private java.util.ArrayList<com.example.askidaayemek.dataClass.urun> filtreliListe;
    private com.example.askidaayemek.adapter.urunAnaSayfaAdapter adapter;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String kullaniciRolu = "MUSTERI";
    
    public urunAnaSayfa() {
        super();
    }
    
    private final com.example.askidaayemek.databinding.FragmentUrunAnaSayfaBinding getBinding() {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void listeyiGuncelleVeMesajKontrolEt() {
    }
    
    private final void arayuzuRoleGoreDuzenle() {
    }
    
    private final void kullaniciBilgisiniGetir() {
    }
    
    private final void aramaYap(java.lang.String text) {
    }
    
    private final void kategoriFiltrele(java.lang.String kategori) {
    }
    
    private final void verileriGetir() {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
}