package com.example.askidaayemek.view;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013H\u0002J\b\u0010\u0014\u001a\u00020\u0011H\u0002J\b\u0010\u0015\u001a\u00020\u0011H\u0016J\u001a\u0010\u0016\u001a\u00020\u00112\u0006\u0010\u0017\u001a\u00020\u00182\b\u0010\u0019\u001a\u0004\u0018\u00010\u001aH\u0016J\u0010\u0010\u001b\u001a\u00020\u00112\u0006\u0010\u001c\u001a\u00020\u0004H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\t\u001a\u00020\u00068BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001d"}, d2 = {"Lcom/example/askidaayemek/view/yoneticiQrKodFragment;", "Landroidx/fragment/app/Fragment;", "()V", "TAG", "", "_binding", "Lcom/example/askidaayemek/databinding/FragmentYoneticiQrKodBinding;", "aktifTalepId", "asilUrunId", "binding", "getBinding", "()Lcom/example/askidaayemek/databinding/FragmentYoneticiQrKodBinding;", "db", "Lcom/google/firebase/firestore/FirebaseFirestore;", "talepEdilenMiktar", "", "commitBatch", "", "batch", "Lcom/google/firebase/firestore/WriteBatch;", "navigateToAnaSayfa", "onDestroyView", "onViewCreated", "view", "Landroid/view/View;", "savedInstanceState", "Landroid/os/Bundle;", "veriyiIsle", "data", "app_debug"})
public final class yoneticiQrKodFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private com.example.askidaayemek.databinding.FragmentYoneticiQrKodBinding _binding;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.firestore.FirebaseFirestore db = null;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String aktifTalepId;
    @org.jetbrains.annotations.Nullable()
    private java.lang.String asilUrunId;
    private int talepEdilenMiktar = 1;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String TAG = "YoneticiQrKodFragment";
    
    public yoneticiQrKodFragment() {
        super();
    }
    
    private final com.example.askidaayemek.databinding.FragmentYoneticiQrKodBinding getBinding() {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void commitBatch(com.google.firebase.firestore.WriteBatch batch) {
    }
    
    private final void veriyiIsle(java.lang.String data) {
    }
    
    private final void navigateToAnaSayfa() {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
}