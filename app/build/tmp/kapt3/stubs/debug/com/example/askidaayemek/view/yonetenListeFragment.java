package com.example.askidaayemek.view;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0010\u001a\u00020\u0011H\u0016J\u001a\u0010\u0012\u001a\u00020\u00112\u0006\u0010\u0013\u001a\u00020\u00142\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0016J,\u0010\u0017\u001a\u00020\u00112\"\u0010\u0018\u001a\u001e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u000f0\u000ej\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u000f`\u0019H\u0002J\b\u0010\u001a\u001a\u00020\u0011H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\tR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\f\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u000f0\u000e0\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2 = {"Lcom/example/askidaayemek/view/yonetenListeFragment;", "Landroidx/fragment/app/Fragment;", "()V", "_binding", "Lcom/example/askidaayemek/databinding/FragmentYonetenListeBinding;", "adapter", "Lcom/example/askidaayemek/adapter/yonetenListeAdapter;", "binding", "getBinding", "()Lcom/example/askidaayemek/databinding/FragmentYonetenListeBinding;", "db", "Lcom/google/firebase/firestore/FirebaseFirestore;", "teslimatListesi", "Ljava/util/ArrayList;", "Ljava/util/HashMap;", "", "onDestroyView", "", "onViewCreated", "view", "Landroid/view/View;", "savedInstanceState", "Landroid/os/Bundle;", "talepDetayiniGoster", "talep", "Lkotlin/collections/HashMap;", "verileriGetir", "app_debug"})
public final class yonetenListeFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private com.example.askidaayemek.databinding.FragmentYonetenListeBinding _binding;
    @org.jetbrains.annotations.NotNull()
    private final com.google.firebase.firestore.FirebaseFirestore db = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.ArrayList<java.util.HashMap<java.lang.String, java.lang.String>> teslimatListesi = null;
    private com.example.askidaayemek.adapter.yonetenListeAdapter adapter;
    
    public yonetenListeFragment() {
        super();
    }
    
    private final com.example.askidaayemek.databinding.FragmentYonetenListeBinding getBinding() {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void verileriGetir() {
    }
    
    private final void talepDetayiniGoster(java.util.HashMap<java.lang.String, java.lang.String> talep) {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
}