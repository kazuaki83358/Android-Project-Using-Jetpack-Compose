import com.example.animewallpaperapp.Api.SearchResponse
import com.example.animewallpaperapp.Api.WallpaperInfoResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface WallhavenService {

    // Fetch specific wallpaper info by ID
    @GET("/api/v1/w/{id}")
    suspend fun getWallpaperInfo(
        @Path("id") wallpaperId: String,
        @Query("apikey") apiKey: String
    ): WallpaperInfoResponse

    // Search for wallpapers with optional query parameters
    @GET("/api/v1/search")
    suspend fun searchWallpapers(
        @Query("q") query: String? = null,              // Optional search query
        @Query("apikey") apiKey: String = "EQKlRrtZyjmiNE4V2az5Qocukt1Dgylh",                // API key for authenticated users
        @Query("page") page: Int? = 1,                  // Pagination
        @Query("categories") categories: String?,// Categories filter
        @Query("purity") purity: String? = null,        // Purity filter (SFW, NSFW, etc.)
        @Query("sorting") sorting: String? = null,      // Sorting method (e.g., date_added, relevance, etc.)
        @Query("order") order: String?,          // Sorting order (asc, desc)
        @QueryMap otherParams: Map<String, String> = emptyMap() // Additional parameters
    ): SearchResponse
}
