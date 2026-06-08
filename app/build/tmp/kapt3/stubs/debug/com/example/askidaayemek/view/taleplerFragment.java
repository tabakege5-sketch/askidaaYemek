package com.example.askidaayemek.view;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000X\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\f\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J$\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00172\b\u0010\u0018\u001a\u0004\u0018\u00010\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0016J\b\u0010\u001c\u001a\u00020\u001dH\u0016J\u001a\u0010\u001e\u001a\u00020\u001d2\u0006\u0010\u001f\u001a\u00020\u00152\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0016J\b\u0010 \u001a\u00020\u001dH\u0002J\u0018\u0010!\u001a\u00020\u001d2\u0006\u0010\"\u001a\u00020\u000f2\u0006\u0010#\u001a\u00020\u0011H\u0002J\u0010\u0010$\u001a\u00020\u001d2\u0006\u0010\"\u001a\u00020\u000fH\u0002J\u0018\u0010%\u001a\u00020\u001d2\u0006\u0010\"\u001a\u00020\u000f2\u0006\u0010#\u001a\u00020\u0011H\u0002J\b\u0010&\u001a\u00020\u001dH\u0002J\b\u0010\'\u001a\u00020\u001dH\u0002J\b\u0010(\u001a\u00020\u001dH\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\rX\u0082.\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0013X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006)"}, d2 = {"Lcom/example/askidaayemek/view/taleplerFragment;", "Landroidx/fragment/app/Fragment;", "()V", "_binding", "Lcom/example/askidaayemek/databinding/FragmentTaleplerBinding;", "adapter", "Lcom/example/askidaayemek/adapter/talepAdapter;", "auth", "Lcom/google/firebase/auth/FirebaseAuth;", "binding", "getBinding", "()Lcom/example/askidaayemek/databinding/FragmentTaleplerBinding;", "db", "Lcom/google/firebase/firestore/FirebaseFirestore;", "secilenAnlikTalep", "Lcom/example/askidaayemek/dataClass/urun;", "secilenPosition", "", "talepListesi", "Ljava/util/ArrayList;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "", "onViewCreated", "view", "qrIsleminiBaslat", "silmeOnayIletisimiGoster", "talep", "position", "talebiOnaylaVeMiktariDus", "talepIptalEt", "verileriGetir", "yedekSorguCalistir", "yoneticiKorumasiKontrolEt", "app_debug"})
public final class taleplerFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private com.example.askidaayemek.databinding.FragmentTaleplerBinding _binding;
    private com.example.askidaayemek.adapter.talepAdapter adapter;
    @org.jetbrains.annotations.NotNull()
    private java.util.ArrayList<com.example.askidaayemek.dataClass.urun> talepListesi;
    private com.google.firebase.firestore.FirebaseFirestore db;
    private com.google.firebase.auth.FirebaseAuth auth;
    @org.jetbrains.annotations.Nullable()
    private com.example.askidaayemek.dataClass.urun secilenAnlikTalep;
    private int secilenPosition = -1;
    
    public taleplerFragment() {
        super();
    }
    
    private final com.example.askidaayemek.databinding.FragmentTaleplerBinding getBinding() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void talebiOnaylaVeMiktariDus(com.example.askidaayemek.dataClass.urun talep) {
    }
    
    private final void yoneticiKorumasiKontrolEt() {
    }
    
    private final void silmeOnayIletisimiGoster(com.example.askidaayemek.dataClass.urun talep, int position) {
    }
    
    private final void talepIptalEt(com.example.askidaayemek.dataClass.urun talep, int position) {
    }
    
    private final void qrIsleminiBaslat() {
    }
    
    private final void verileriGetir() {
    }
    
    private final void yedekSorguCalistir() {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
}