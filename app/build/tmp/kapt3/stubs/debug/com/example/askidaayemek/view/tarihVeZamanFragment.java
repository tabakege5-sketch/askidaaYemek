package com.example.askidaayemek.view;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000^\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0018\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00042\u0006\u0010\u0019\u001a\u00020\u0004H\u0002J\u0018\u0010\u001a\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\u00042\u0006\u0010\u0019\u001a\u00020\u0004H\u0002J\b\u0010\u001b\u001a\u00020\u0017H\u0002J$\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001f2\b\u0010 \u001a\u0004\u0018\u00010!2\b\u0010\"\u001a\u0004\u0018\u00010#H\u0016J\b\u0010$\u001a\u00020\u0017H\u0016J\u001a\u0010%\u001a\u00020\u00172\u0006\u0010&\u001a\u00020\u001d2\b\u0010\"\u001a\u0004\u0018\u00010#H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\u00020\u00068BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001c\u0010\u000e\u001a\u0010\u0012\f\u0012\n \u0010*\u0004\u0018\u00010\u00040\u00040\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0011\u001a\u0004\u0018\u00010\u0012X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\'"}, d2 = {"Lcom/example/askidaayemek/view/tarihVeZamanFragment;", "Landroidx/fragment/app/Fragment;", "()V", "BILDIRIM_KANAL_ID", "", "_binding", "Lcom/example/askidaayemek/databinding/FragmentTarihVeZamanBinding;", "auth", "Lcom/google/firebase/auth/FirebaseAuth;", "binding", "getBinding", "()Lcom/example/askidaayemek/databinding/FragmentTarihVeZamanBinding;", "db", "Lcom/google/firebase/firestore/FirebaseFirestore;", "izinIsteyici", "Landroidx/activity/result/ActivityResultLauncher;", "kotlin.jvm.PlatformType", "mevcutUrun", "Lcom/example/askidaayemek/dataClass/urun;", "secilenTarih", "talepEdilenMiktar", "", "bildirimGoster", "", "baslik", "mesaj", "bildirimIzinKontrolEtVeGoster", "bildirimKanaliOlustur", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onViewCreated", "view", "app_debug"})
public final class tarihVeZamanFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private com.example.askidaayemek.databinding.FragmentTarihVeZamanBinding _binding;
    @org.jetbrains.annotations.NotNull()
    private java.lang.String secilenTarih = "";
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.firestore.FirebaseFirestore db = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.auth.FirebaseAuth auth = null;
    @org.jetbrains.annotations.Nullable()
    private com.example.askidaayemek.dataClass.urun mevcutUrun;
    private int talepEdilenMiktar = 1;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String BILDIRIM_KANAL_ID = "rezervasyon_kanali";
    @org.jetbrains.annotations.NotNull()
    private final androidx.activity.result.ActivityResultLauncher<java.lang.String> izinIsteyici = null;
    
    public tarihVeZamanFragment() {
        super();
    }
    
    private final com.example.askidaayemek.databinding.FragmentTarihVeZamanBinding getBinding() {
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
    
    private final void bildirimKanaliOlustur() {
    }
    
    private final void bildirimIzinKontrolEtVeGoster(java.lang.String baslik, java.lang.String mesaj) {
    }
    
    private final void bildirimGoster(java.lang.String baslik, java.lang.String mesaj) {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
}