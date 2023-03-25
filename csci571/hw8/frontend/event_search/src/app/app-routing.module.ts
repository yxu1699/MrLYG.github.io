import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SearchformComponent } from './searchform/searchform.component';
import { AutocomComponent } from './autocom/autocom.component';
import { FavoritesComponent } from './favorites/favorites.component'

const routes: Routes = [
  { path: 'search', component: SearchformComponent },
  { path: 'favorites', component: FavoritesComponent },
  { path: '', redirectTo: '/search', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
