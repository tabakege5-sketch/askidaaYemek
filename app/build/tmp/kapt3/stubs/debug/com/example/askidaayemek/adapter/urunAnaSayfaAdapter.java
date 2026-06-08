package com.example.askidaayemek.adapter;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u0014B1\u0012\u0016\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u0004j\b\u0012\u0004\u0012\u00020\u0005`\u0006\u0012\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\t0\b\u00a2\u0006\u0002\u0010\nJ\b\u0010\u000b\u001a\u00020\fH\u0016J\u0018\u0010\r\u001a\u00020\t2\u0006\u0010\u000e\u001a\u00020\u00022\u0006\u0010\u000f\u001a\u00020\fH\u0016J\u0018\u0010\u0010\u001a\u00020\u00022\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\fH\u0016R\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u0004j\b\u0012\u0004\u0012\u00020\u0005`\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0015"}, d2 = {"Lcom/example/askidaayemek/adapter/urunAnaSayfaAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/example/askidaayemek/adapter/urunAnaSayfaAdapter$urunHolder;", "urunListesi", "Ljava/util/ArrayList;", "Lcom/example/askidaayemek/dataClass/urun;", "Lkotlin/collections/ArrayList;", "onItemClick", "Lkotlin/Function1;", "", "(Ljava/util/ArrayList;Lkotlin/jvm/functions/Function1;)V", "getItemCount", "", "onBindViewHolder", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "urunHolder", "app_debug"})
public final class urunAnaSayfaAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.example.askidaayemek.adapter.urunAnaSayfaAdapter.urunHolder> {
    @org.jetbrains.annotations.NotNull()
    private final java.util.ArrayList<com.example.askidaayemek.dataClass.urun> urunListesi = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function1<com.example.askidaayemek.dataClass.urun, kotlin.Unit> onItemClick = null;
    
    public urunAnaSayfaAdapter(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<com.example.askidaayemek.dataClass.urun> urunListesi, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.example.askidaayemek.dataClass.urun, kotlin.Unit> onItemClick) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public com.example.askidaayemek.adapter.urunAnaSayfaAdapter.urunHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.example.askidaayemek.adapter.urunAnaSayfaAdapter.urunHolder holder, int position) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/example/askidaayemek/adapter/urunAnaSayfaAdapter$urunHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/example/askidaayemek/databinding/UrunRecyclerRowBinding;", "(Lcom/example/askidaayemek/databinding/UrunRecyclerRowBinding;)V", "getBinding", "()Lcom/example/askidaayemek/databinding/UrunRecyclerRowBinding;", "app_debug"})
    public static final class urunHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final com.example.askidaayemek.databinding.UrunRecyclerRowBinding binding = null;
        
        public urunHolder(@org.jetbrains.annotations.NotNull()
        com.example.askidaayemek.databinding.UrunRecyclerRowBinding binding) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.askidaayemek.databinding.UrunRecyclerRowBinding getBinding() {
            return null;
        }
    }
}