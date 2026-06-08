package com.example.askidaayemek.adapter;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0001\u0018B_\u0012\u0016\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u0004j\b\u0012\u0004\u0012\u00020\u0005`\u0006\u0012\u0012\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\t0\b\u0012\u0012\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\t0\b\u0012\u0018\u0010\u000b\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\t0\f\u00a2\u0006\u0002\u0010\u000eJ\b\u0010\u000f\u001a\u00020\rH\u0016J\u0018\u0010\u0010\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\u00022\u0006\u0010\u0012\u001a\u00020\rH\u0016J\u0018\u0010\u0013\u001a\u00020\u00022\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\rH\u0016J\u000e\u0010\u0017\u001a\u00020\t2\u0006\u0010\u0012\u001a\u00020\rR \u0010\u000b\u001a\u0014\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\r\u0012\u0004\u0012\u00020\t0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0005\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001e\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u0004j\b\u0012\u0004\u0012\u00020\u0005`\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/example/askidaayemek/adapter/talepAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/example/askidaayemek/adapter/talepAdapter$talepHolder;", "talepListesi", "Ljava/util/ArrayList;", "Lcom/example/askidaayemek/dataClass/urun;", "Lkotlin/collections/ArrayList;", "onItemClick", "Lkotlin/Function1;", "", "onOnaylaClick", "onIptalClick", "Lkotlin/Function2;", "", "(Ljava/util/ArrayList;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function2;)V", "getItemCount", "onBindViewHolder", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "siraliElemanSil", "talepHolder", "app_debug"})
public final class talepAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.example.askidaayemek.adapter.talepAdapter.talepHolder> {
    @org.jetbrains.annotations.NotNull()
    private final java.util.ArrayList<com.example.askidaayemek.dataClass.urun> talepListesi = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function1<com.example.askidaayemek.dataClass.urun, kotlin.Unit> onItemClick = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function1<com.example.askidaayemek.dataClass.urun, kotlin.Unit> onOnaylaClick = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.jvm.functions.Function2<com.example.askidaayemek.dataClass.urun, java.lang.Integer, kotlin.Unit> onIptalClick = null;
    
    public talepAdapter(@org.jetbrains.annotations.NotNull()
    java.util.ArrayList<com.example.askidaayemek.dataClass.urun> talepListesi, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.example.askidaayemek.dataClass.urun, kotlin.Unit> onItemClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super com.example.askidaayemek.dataClass.urun, kotlin.Unit> onOnaylaClick, @org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function2<? super com.example.askidaayemek.dataClass.urun, ? super java.lang.Integer, kotlin.Unit> onIptalClick) {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public com.example.askidaayemek.adapter.talepAdapter.talepHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.example.askidaayemek.adapter.talepAdapter.talepHolder holder, int position) {
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    public final void siraliElemanSil(int position) {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/example/askidaayemek/adapter/talepAdapter$talepHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "binding", "Lcom/example/askidaayemek/databinding/TalepLayoutBinding;", "(Lcom/example/askidaayemek/databinding/TalepLayoutBinding;)V", "getBinding", "()Lcom/example/askidaayemek/databinding/TalepLayoutBinding;", "app_debug"})
    public static final class talepHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final com.example.askidaayemek.databinding.TalepLayoutBinding binding = null;
        
        public talepHolder(@org.jetbrains.annotations.NotNull()
        com.example.askidaayemek.databinding.TalepLayoutBinding binding) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull()
        public final com.example.askidaayemek.databinding.TalepLayoutBinding getBinding() {
            return null;
        }
    }
}