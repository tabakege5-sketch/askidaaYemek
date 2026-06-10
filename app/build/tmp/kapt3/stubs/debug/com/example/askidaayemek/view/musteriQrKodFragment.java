package com.example.askidaayemek.view;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000H\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0006H\u0002J\b\u0010\u0014\u001a\u00020\u0015H\u0016J\u001a\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0016J\u0018\u0010\u001b\u001a\u00020\u00152\u0006\u0010\u001c\u001a\u00020\u00062\u0006\u0010\u001d\u001a\u00020\u0006H\u0002J\b\u0010\u001e\u001a\u00020\u0015H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\tR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u000e\u001a\u0004\u0018\u00010\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001f"}, d2 = {"Lcom/example/askidaayemek/view/musteriQrKodFragment;", "Landroidx/fragment/app/Fragment;", "()V", "_binding", "Lcom/example/askidaayemek/databinding/FragmentMusteriQrKodBinding;", "asilIlanId", "", "binding", "getBinding", "()Lcom/example/askidaayemek/databinding/FragmentMusteriQrKodBinding;", "db", "Lcom/google/firebase/firestore/FirebaseFirestore;", "kullaniciAdSoyad", "kullaniciEmail", "musteriTalepDinleyici", "Lcom/google/firebase/firestore/ListenerRegistration;", "secilenUrunId", "musteriQrOlustur", "Landroid/graphics/Bitmap;", "veriler", "onDestroyView", "", "onViewCreated", "view", "Landroid/view/View;", "savedInstanceState", "Landroid/os/Bundle;", "qrKoduUretVeEkranaBas", "talepEdilenMiktar", "secilenUrunAdi", "takipEtYoneticiOnayini", "app_debug"})
public final class musteriQrKodFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private com.example.askidaayemek.databinding.FragmentMusteriQrKodBinding _binding;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.firestore.FirebaseFirestore db = null;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String kullaniciAdSoyad = "Bilinmeyen Kullan\u0131c\u0131";
    @org.jetbrains.annotations.NotNull()
    private java.lang.String kullaniciEmail = "E-posta Bulunamad\u0131";
    @org.jetbrains.annotations.Nullable()
    private java.lang.String secilenUrunId;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String asilIlanId;
    @org.jetbrains.annotations.Nullable()
    private com.google.firebase.firestore.ListenerRegistration musteriTalepDinleyici;
    
    public musteriQrKodFragment() {
        super();
    }
    
    private final com.example.askidaayemek.databinding.FragmentMusteriQrKodBinding getBinding() {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void qrKoduUretVeEkranaBas(java.lang.String talepEdilenMiktar, java.lang.String secilenUrunAdi) {
    }
    
    private final android.graphics.Bitmap musteriQrOlustur(java.lang.String veriler) {
        return null;
    }
    
    private final void takipEtYoneticiOnayini() {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
}